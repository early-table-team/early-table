package com.gotcha.earlytable.domain.file.enums;

import lombok.Getter;

@Getter
public enum FileStatus {

    REPRESENTATIVE("대표"),
    REGULAR("일반");

    private final String fileStatusName;

    FileStatus(String fileStatusName) {
        this.fileStatusName = fileStatusName;
    }
}
