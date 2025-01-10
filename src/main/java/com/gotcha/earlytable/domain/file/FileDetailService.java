package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.dto.FileDetailDto;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.file.enums.FileType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.Arrays;

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
            if(!isImageExtension(extension)) {
                throw new IllegalArgumentException("지원되는 이미지 형식이 아닙니다.");
            }

            FileDetailDto fileDetailDto = uploadFile(imageFile);

            FileDetail fileDetail =
                    FileDetail.toEntity(FileStatus.REPRESENTATIVE, file, 1, FileType.valueOf(extension), fileDetailDto);

            FileDetail savedFileDetail = fileDetailRepository.save(fileDetail);

            return savedFileDetail.getFileUrl();

        } catch (IOException e) {
            throw new IllegalArgumentException("업로드에 실패하였습니다.");
        }
    }


    /**
     * 이미지 파일 제거 메서드
     *
     * @param uniqueFileUrl
     */
    public void deleteImageFile(String uniqueFileUrl) {

        FileDetail fileDetail = fileDetailRepository.findByFileUrl(uniqueFileUrl);

        // s3 서버에서 이미지 삭제
        deleteFile(fileDetail.getFileUniqueName());

        // 정보 데이터 삭제
        fileDetailRepository.delete(fileDetail);
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

        return Arrays.stream(FileType.values()).anyMatch(fileType -> fileType.getExtension().equals(extension));
    }


}
