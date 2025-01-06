package com.gotcha.earlytable.domain.file;

import lombok.Getter;

@Getter
public enum FileType {
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png"),
    GIF("gif"),
    BMP("bmp"),
    TIFF("tiff"),
    WEBP("webp");

    private final String extension;

    FileType(String extension) {
        this.extension = extension;
    }
}
