package com.gotcha.earlytable.domain.file.dto;


import lombok.Getter;

@Getter
public class FileDetailDto {

    private final String fileName;

    private final String fileUniqueName;

    private final String fileUrl;

    private final Long fileSize;


    public FileDetailDto(String fileName, String fileUniqueName, String fileUrl, Long fileSize) {
        this.fileName = fileName;
        this.fileUniqueName = fileUniqueName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
    }
}
