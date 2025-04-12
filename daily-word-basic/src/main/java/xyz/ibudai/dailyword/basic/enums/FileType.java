package xyz.ibudai.dailyword.basic.enums;

import lombok.Getter;

@Getter
public enum FileType {

    JPEG("jpeg", ".jpeg");


    private final String type;

    private final String extension;

    FileType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }
}
