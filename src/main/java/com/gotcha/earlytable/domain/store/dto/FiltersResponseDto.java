package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class FiltersResponseDto {
    Map<String, List<String>> regionMap;
    List<String> categoryList;
    Map<String, List<String>> categoryMap;

    public FiltersResponseDto(Map<String, List<String>> regionMap, List<String> categoryList, Map<String, List<String>> categoryMap) {
        this.regionMap = regionMap;
        this.categoryList = categoryList;
        this.categoryMap = categoryMap;
    }
}