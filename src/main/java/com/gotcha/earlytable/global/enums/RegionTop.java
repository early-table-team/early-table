package com.gotcha.earlytable.global.enums;

import lombok.Getter;

@Getter
public enum RegionTop {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    BUSAN("부산"),
    ULSAN("울산"),
    GYEONGNAM("경남"),
    DAEGU("대구"),
    GYEONGBUK("경북"),
    GWANGJU("광주"),
    JEONNAM("전남"),
    JEONBUK("전북"),
    GANGWON("강원"),
    DAEJEON("대전"),
    CHUNGNAM("충남"),
    CHUNGBUK("충북"),
    JEJU("제주");


    private final String name;


    RegionTop(String name) {
        this.name = name;
    }
}
