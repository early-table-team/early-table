package com.gotcha.earlytable.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class TokenNotificationRequestDto {
    private String title;
    private String content;
    private String url;
    private String img;

    public TokenNotificationRequestDto(String title, String content, String url, String img) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.img = img;
    }


}
