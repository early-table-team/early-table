package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.dto.FileDetailDto;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.file.enums.FileType;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileDetailService extends s3 {


    private final FileDetailRepository fileDetailRepository;


    public FileDetailService(S3Client s3Client, FileDetailRepository fileDetailRepository) {
        super(s3Client);
        this.fileDetailRepository = fileDetailRepository;
    }

    /**
     * 이미지 파일 저장 메서드
     *
     * @param imageFile
     * @param file
     * @return 이미지 file s3 url 반환
     */
    @Transactional
    public String createImageFile(MultipartFile imageFile, File file) {

        try {
            // 확장자 추출
            String extension = getFileExtension(imageFile.getOriginalFilename());

            // 이미지 형식의 파일이 맞는지 확인
            if(isImageExtension(extension)) {
                throw new IllegalArgumentException("지원되는 이미지 형식이 아닙니다.");
            }

            FileDetailDto fileDetailDto = uploadFile(imageFile);

            FileDetail fileDetail =
                    FileDetail.toEntity(FileStatus.REPRESENTATIVE, file, file.getFileDetailList().size()+1,
                            FileType.valueOf(extension.toUpperCase()), fileDetailDto);

            FileDetail savedFileDetail = fileDetailRepository.save(fileDetail);

            return savedFileDetail.getFileUrl();

        } catch (IOException e) {
            throw new IllegalArgumentException("업로드에 실패하였습니다.");
        }
    }

    /**
     * 이미지 파일 리스트 저장 메서드
     *
     * @param imageFiles
     * @param file
     */
    @Transactional
    public void createImageFiles(List<MultipartFile> imageFiles, File file) {

        List<FileDetail> fileDetails = new ArrayList<>();
        int fileSeq = file.getFileDetailList().size() + 1;

        try {
            for (int i = 0; i < imageFiles.size(); i++) {
                // 확장자 추출
                String extension = getFileExtension(imageFiles.get(i).getOriginalFilename());

                // 이미지 형식의 파일이 맞는지 확인
                if (isImageExtension(extension)) {
                    throw new IllegalArgumentException("지원되는 이미지 형식이 아닙니다.");
                }

                FileDetailDto fileDetailDto = uploadFile(imageFiles.get(i));

                // 파일 대표 여부
                FileStatus fileStatus = FileStatus.REGULAR;
                if(i == 0){
                    fileStatus = FileStatus.REPRESENTATIVE;
                }

                // 파일 생성
                FileDetail fileDetail =
                        FileDetail.toEntity(fileStatus, file, fileSeq, FileType.valueOf(extension), fileDetailDto);

                fileDetails.add(fileDetail);

                // 순서 증가
                fileSeq++;

            }
        } catch (IOException e) {
            throw new IllegalArgumentException("업로드에 실패하였습니다.");
        }

        fileDetailRepository.saveAll(fileDetails);
    }

    /**
     * 파일 디테일 복사하는 메서드
     *
     * @param fileDetails 복사하려는 파일 디테일
     * @param file 새롭게 복사하려는 파일
     */
    public void copyFileDetails(List<FileDetail> fileDetails, File file) {

        List<FileDetail> newFileDetails = new ArrayList<>();

        for (FileDetail fileDetail : fileDetails) {
            FileDetail newFileDetail = new FileDetail(fileDetail.getFileName(), fileDetail.getFileUniqueName(),
                    fileDetail.getFileUrl(), fileDetail.getFileType(), fileDetail.getFileStatus(),
                    fileDetail.getFileSize(), file, fileDetail.getFileSeq());

            newFileDetails.add(newFileDetail);
        }

        fileDetailRepository.saveAll(newFileDetails);
    }

    /**
     * 이미지 업데이트
     *
     * @param fileList 새롭게 추가하는 파일들
     * @param fileUrlList 현재 수정하려는 파일들의 순서 정렬된 파일 이름 리스트
     * @param file
     */
    @Transactional
    public void updateFileDetail(List<MultipartFile> fileList, List<String> fileUrlList, File file) {

        // 새로운 파일 등록
        if (fileList != null && fileList.size() > 1) {
            createImageFiles(fileList, file);
        }

        // 기존 파일 리스트 가져오기
        List<FileDetail> fileDetailList = file.getFileDetailList();

        // 순서 리스트가 더 크면 잘못된 값
        if (fileUrlList.size() > fileDetailList.size()) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 파일 이름을 URL로 변경을 위한 map 생성
        Map<String, String> fileNameToUrlMap = fileDetailList.stream()
                .collect(Collectors.toMap(FileDetail::getFileName, FileDetail::getFileUrl));

        // 파일 이름을 URL로 변경
        for (int i = 0; i < fileUrlList.size(); i++) {
            String identifier = fileUrlList.get(i);

            // fileName이 fileUrlList에 존재하면 fileUrl로 교체
            if (fileNameToUrlMap.containsKey(identifier)) {
                fileUrlList.set(i, fileNameToUrlMap.get(identifier));
            }
        }

        // 삭제할 파일 처리
        fileDetailList.forEach(fileDetail -> {
            String fileUrl = fileDetail.getFileUrl();
            if (!fileUrlList.contains(fileUrl)) {
                deleteFile(fileUrl);
            }
        });

        // 기존 파일을 Map으로 변환하여 빠르게 검색 가능하도록 구성
        Map<String, FileDetail> fileDetailMap = fileDetailList.stream()
                .collect(Collectors.toMap(FileDetail::getFileUrl, fileDetail -> fileDetail));

        // 순서 변경 및 상태 업데이트
        for (int i = 0; i < fileUrlList.size(); i++) {
            String identifier = fileUrlList.get(i);
            FileDetail fileDetail = fileDetailMap.get(identifier);

            if (fileDetail != null) {
                // 순서 업데이트
                fileDetail.updateSeq(i + 1);

                // 대표 이미지 상태 변경
                fileDetail.updateFileStatus(i == 0 ? FileStatus.REPRESENTATIVE : FileStatus.REGULAR);
            }
        }
    }

    /**
     * 이미지 파일 제거 메서드
     *
     * @param uniqueFileUrl
     */
    @Transactional
    public void deleteImageFile(String uniqueFileUrl) {

        FileDetail fileDetail = fileDetailRepository.findByFileUrl(uniqueFileUrl);

        // s3 서버에서 이미지 삭제
        deleteFile(fileDetail.getFileUniqueName());

        // 정보 데이터 삭제
        fileDetailRepository.delete(fileDetail);
    }

    /**
     * 이미지 파일 리스트 제거 메서드
     *
     * @param uniqueFileUrls
     */
    @Transactional
    public void deleteImageFiles(List<String> uniqueFileUrls) {

        fileDetailRepository.deleteByFileUrlIn(uniqueFileUrls);
    }

    /**
     * 파일 이름에서 확장자를 추출하는 메서드
     *
     * @param fileName 파일 이름
     * @return 확장자 (확장자가 없을 경우 빈 문자열 반환)
     */
    public static String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            return ""; // 확장자가 없거나 파일 이름이 비어 있는 경우
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == fileName.length() - 1) {
            return ""; // 파일 이름이 '.'으로 끝나는 경우
        }

        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * 이미지 파일인지 확인하는 메서드
     *
     * @param extension
     * @return 이미지 확인 여부 true/false
     */
    public static boolean isImageExtension(String extension) {

        return Arrays.stream(FileType.values()).noneMatch(fileType -> fileType.getExtension().equals(extension));
    }

}
