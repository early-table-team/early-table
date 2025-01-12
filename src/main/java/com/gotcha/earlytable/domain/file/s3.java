package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.dto.FileDetailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;


public class s3 {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    public s3(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * s3 서버 파일 업로드 메서드
     *
     * @param file
     * @return
     * @throws IOException
     */
    public FileDetailDto uploadFile(MultipartFile file) throws IOException {

        // 파일 s3에 업로드하고 DB에 정보 저장
        String originalFilename = file.getOriginalFilename();

        if(originalFilename == null) {
            return null;
        }

        // 고유 파일 이름 생성
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        // S3에 파일 업로드
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        FileDetailDto fileDetailDto =
                new FileDetailDto(originalFilename, fileName, getPublicUrl(fileName), file.getSize());

        // S3 서버 객체 URL 가져오기
        return fileDetailDto;
    }


    /**
     * s3 서버 파일 제거 메서드
     *
     * @param uniqueFileName
     */
    public void deleteFile(String uniqueFileName) {

        // S3 에서 파일 제거
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(uniqueFileName).build());
    }

    /**
     * S3 서버 경로 가져오는 메서드
     *
     * @param fileName
     * @return S3 file url
     */
    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, fileName);
    }


}
