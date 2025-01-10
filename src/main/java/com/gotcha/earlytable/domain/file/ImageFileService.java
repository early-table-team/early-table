package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.ImageFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    /**
     * 이미지 파일 저장 메서드
     *
     * @param imageFile
     * @param file
     * @return String
     */
    public String createImageFile(MultipartFile imageFile, File file) {

        ImageFile savedImageFile = imageFileRepository.save(new ImageFile());

        return savedImageFile.getFileUniqueName();
    }


    public void deleteImageFile(String fileUniqueName) {}
}
