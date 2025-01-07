package com.gotcha.earlytable.domain.file;

import com.gotcha.earlytable.domain.file.entity.File;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * 파일 생성 메서드
     *
     * @return File
     */
    public File createFile() {
        File file = new File();

        return fileRepository.save(file);
    }
}
