package com.gotcha.earlytable.domain.menu.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MenuSearchRequestDto {

    private final String searchWord;

    private final List<String> allergyCategory;

    private final List<String> allergyStuff;


    public MenuSearchRequestDto(String searchWord, List<String> allergyCategory, List<String> allergyStuff) {
        this.searchWord = searchWord;
        this.allergyCategory = allergyCategory;
        this.allergyStuff = allergyStuff;
    }
}
