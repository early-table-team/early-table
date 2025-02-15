package com.gotcha.earlytable.global.enums;

import lombok.Getter;

@Getter
public enum RegionBottom {
    // 서울
    JONGNO(RegionTop.SEOUL, "종로구"),
    JUNG(RegionTop.SEOUL, "중구"),
    YONGSAN(RegionTop.SEOUL, "용산구"),
    SEONGDONG(RegionTop.SEOUL, "성동구"),
    GWANGJIN(RegionTop.SEOUL, "광진구"),
    DONGDAEMUN(RegionTop.SEOUL, "동대문구"),
    JUNGNANG(RegionTop.SEOUL, "중랑구"),
    SEONGBUK(RegionTop.SEOUL, "성북구"),
    GANGBUK(RegionTop.SEOUL, "강북구"),
    DOBONG(RegionTop.SEOUL, "도봉구"),
    NOWON(RegionTop.SEOUL, "노원구"),
    EUNPYEONG(RegionTop.SEOUL, "은평구"),
    SEODAEMUN(RegionTop.SEOUL, "서대문구"),
    MAPO(RegionTop.SEOUL, "마포구"),
    YANGCHEON(RegionTop.SEOUL, "양천구"),
    GANGSEO(RegionTop.SEOUL, "강서구"),
    GURO(RegionTop.SEOUL, "구로구"),
    GEUMCHEON(RegionTop.SEOUL, "금천구"),
    YEONGDEUNGPO(RegionTop.SEOUL, "영등포구"),
    DONGJAK(RegionTop.SEOUL, "동작구"),
    GWANAK(RegionTop.SEOUL, "관악구"),
    SEOCHO(RegionTop.SEOUL, "서초구"),
    GANGNAM(RegionTop.SEOUL, "강남구"),
    SONGPA(RegionTop.SEOUL, "송파구"),
    GANGDONG(RegionTop.SEOUL, "강동구"),

    // 경기
    SUWON(RegionTop.GYEONGGI, "수원시"),
    SEONGNAM(RegionTop.GYEONGGI, "성남시"),
    GOYANG(RegionTop.GYEONGGI, "고양시"),
    YONGIN(RegionTop.GYEONGGI, "용인시"),
    BUCHEON(RegionTop.GYEONGGI, "부천시"),
    ANSAN(RegionTop.GYEONGGI, "안산시"),
    ANYANG(RegionTop.GYEONGGI, "안양시"),
    NAMYANGJU(RegionTop.GYEONGGI, "남양주시"),
    HWASEONG(RegionTop.GYEONGGI, "화성시"),
    PYEONGTAEK(RegionTop.GYEONGGI, "평택시"),
    UIJEONGBU(RegionTop.GYEONGGI, "의정부시"),
    SIHEUNG(RegionTop.GYEONGGI, "시흥시"),
    GIMPO(RegionTop.GYEONGGI, "김포시"),
    GWANGMYEONG(RegionTop.GYEONGGI, "광명시"),
    GUNPO(RegionTop.GYEONGGI, "군포시"),
    GWACHEON(RegionTop.GYEONGGI, "과천시"),
    OSAN(RegionTop.GYEONGGI, "오산시"),
    ICHEON(RegionTop.GYEONGGI, "이천시"),
    UIWANG(RegionTop.GYEONGGI, "의왕시"),
    HANAM(RegionTop.GYEONGGI, "하남시"),
    POCHEON(RegionTop.GYEONGGI, "포천시"),
    GWANGJU(RegionTop.GYEONGGI, "광주시"),
    YEOJU(RegionTop.GYEONGGI, "여주시"),
    YEONCHEON(RegionTop.GYEONGGI, "연천군"),
    GAPYEONG(RegionTop.GYEONGGI, "가평군"),
    YANGPYEONG(RegionTop.GYEONGGI, "양평군"),
    DONGDUCHEON(RegionTop.GYEONGGI, "동두천시"),
    YANGJU(RegionTop.GYEONGGI, "양주시"),

    // 인천
    // 인천광역시
    JUNG_INCHEON(RegionTop.INCHEON, "중구"),
    DONG_INCHEON(RegionTop.INCHEON, "동구"),
    MIJANG(RegionTop.INCHEON, "미추홀구"),
    YEONSEO(RegionTop.INCHEON, "연수구"),
    NAM_INCHEON(RegionTop.INCHEON, "남동구"),
    BUPYEONG(RegionTop.INCHEON, "부평구"),
    GYESAN(RegionTop.INCHEON, "계양구"),
    SEO_INCHEON(RegionTop.INCHEON, "서구"),
    GANGHWA(RegionTop.INCHEON, "강화군"),
    ONGJIN(RegionTop.INCHEON, "옹진군"),

    // 부산광역시
    JUNG_BUSAN(RegionTop.BUSAN, "중구"),
    SEO_BUSAN(RegionTop.BUSAN, "서구"),
    DONG_BUSAN(RegionTop.BUSAN, "동구"),
    YEONGDO(RegionTop.BUSAN, "영도구"),
    BUSANJIN(RegionTop.BUSAN, "부산진구"),
    DONGNAE(RegionTop.BUSAN, "동래구"),
    NAM_BUSAN(RegionTop.BUSAN, "남구"),
    BUK_BUSAN(RegionTop.BUSAN, "북구"),
    HAEUNDAE(RegionTop.BUSAN, "해운대구"),
    SAHA(RegionTop.BUSAN, "사하구"),
    GEUMJEONG(RegionTop.BUSAN, "금정구"),
    GANGSEO_BUSAN(RegionTop.BUSAN, "강서구"),
    YEONJE(RegionTop.BUSAN, "연제구"),
    SUYEONG(RegionTop.BUSAN, "수영구"),
    SASANG(RegionTop.BUSAN, "사상구"),
    GIJANG(RegionTop.BUSAN, "기장군"),

    // 울산광역시
    JUNG_ULSAN(RegionTop.ULSAN, "중구"),
    NAM_ULSAN(RegionTop.ULSAN, "남구"),
    DONG_ULSAN(RegionTop.ULSAN, "동구"),
    BUK_ULSAN(RegionTop.ULSAN, "북구"),
    ULJU(RegionTop.ULSAN, "울주군"),

    // 경상남도
    CHANGWON(RegionTop.GYEONGNAM, "창원시"),
    JINJU(RegionTop.GYEONGNAM, "진주시"),
    TONGYEONG(RegionTop.GYEONGNAM, "통영시"),
    SACHEON(RegionTop.GYEONGNAM, "사천시"),
    GIMHAE(RegionTop.GYEONGNAM, "김해시"),
    MILYANG(RegionTop.GYEONGNAM, "밀양시"),
    GEOJE(RegionTop.GYEONGNAM, "거제시"),
    YANGSAN(RegionTop.GYEONGNAM, "양산시"),
    UIRYEONG(RegionTop.GYEONGNAM, "의령군"),
    HAMAN(RegionTop.GYEONGNAM, "함안군"),
    CHANGNYEONG(RegionTop.GYEONGNAM, "창녕군"),
    GOSEONG(RegionTop.GYEONGNAM, "고성군"),
    NAMHAE(RegionTop.GYEONGNAM, "남해군"),
    HADONG(RegionTop.GYEONGNAM, "하동군"),
    SANCHEONG(RegionTop.GYEONGNAM, "산청군"),
    HAPCHEON(RegionTop.GYEONGNAM, "합천군"),

    // 대구광역시
    JUNG_DAEGU(RegionTop.DAEGU, "중구"),
    DONG_DAEGU(RegionTop.DAEGU, "동구"),
    SEO_DAEGU(RegionTop.DAEGU, "서구"),
    NAM_DAEGU(RegionTop.DAEGU, "남구"),
    BUK_DAEGU(RegionTop.DAEGU, "북구"),
    SUSEONG(RegionTop.DAEGU, "수성구"),
    DALSEO(RegionTop.DAEGU, "달서구"),
    DALSEONG(RegionTop.DAEGU, "달성군"),

    // 경상북도
    POHANG(RegionTop.GYEONGBUK, "포항시"),
    GYEONGJU(RegionTop.GYEONGBUK, "경주시"),
    GIMCHEON(RegionTop.GYEONGBUK, "김천시"),
    ANDONG(RegionTop.GYEONGBUK, "안동시"),
    GUML(RegionTop.GYEONGBUK, "구미시"),
    YEONGJU(RegionTop.GYEONGBUK, "영주시"),
    YEONGCHEON(RegionTop.GYEONGBUK, "영천시"),
    SANGJU(RegionTop.GYEONGBUK, "상주시"),
    MUNGYEONG(RegionTop.GYEONGBUK, "문경시"),
    GYEONGSAN(RegionTop.GYEONGBUK, "경산시"),
    GUNWI(RegionTop.GYEONGBUK, "군위군"),
    UISEONG(RegionTop.GYEONGBUK, "의성군"),
    CHEONGSONG(RegionTop.GYEONGBUK, "청송군"),
    YEONGYANG(RegionTop.GYEONGBUK, "영양군"),
    YEONGDOK(RegionTop.GYEONGBUK, "영덕군"),
    CHEONGDO(RegionTop.GYEONGBUK, "청도군"),
    GORYEONG(RegionTop.GYEONGBUK, "고령군"),
    SEONGJU(RegionTop.GYEONGBUK, "성주군"),
    CHILGOK(RegionTop.GYEONGBUK, "칠곡군"),
    YEONGHYEON(RegionTop.GYEONGBUK, "예천군"),
    BONGHWA(RegionTop.GYEONGBUK, "봉화군"),
    ULLEUNG(RegionTop.GYEONGBUK, "울릉군"),

    // 광주광역시
    DONG_GWANGJU(RegionTop.GWANGJU, "동구"),
    SEO_GWANGJU(RegionTop.GWANGJU, "서구"),
    NAM_GWANGJU(RegionTop.GWANGJU, "남구"),
    BUK_GWANGJU(RegionTop.GWANGJU, "북구"),
    GWANGSAN(RegionTop.GWANGJU, "광산구"),


    // 전라남도
    MOKPO(RegionTop.JEONNAM, "목포시"),
    YEOSU(RegionTop.JEONNAM, "여수시"),
    SUNCHEON(RegionTop.JEONNAM, "순천시"),
    NAJU(RegionTop.JEONNAM, "나주시"),
    GWANGYANG(RegionTop.JEONNAM, "광양시"),
    DAMYANG(RegionTop.JEONNAM, "담양군"),
    GOHEUNG(RegionTop.JEONNAM, "고흥군"),
    BOSEONG(RegionTop.JEONNAM, "보성군"),
    HWASUN(RegionTop.JEONNAM, "화순군"),
    JANGHEUNG(RegionTop.JEONNAM, "장흥군"),
    GANGJIN(RegionTop.JEONNAM, "강진군"),
    HAENAM(RegionTop.JEONNAM, "해남군"),
    YEONGAM(RegionTop.JEONNAM, "영암군"),
    MUAN(RegionTop.JEONNAM, "무안군"),
    HAMPYEONG(RegionTop.JEONNAM, "함평군"),
    YEONGGWANG(RegionTop.JEONNAM, "영광군"),
    JANGSEONG(RegionTop.JEONNAM, "장성군"),
    WANDO(RegionTop.JEONNAM, "완도군"),
    JINDO(RegionTop.JEONNAM, "진도군"),
    SINAN(RegionTop.JEONNAM, "신안군"),

    // 전라북도
    JEONJU(RegionTop.JEONBUK, "전주시"),
    GUNSAN(RegionTop.JEONBUK, "군산시"),
    IKSAN(RegionTop.JEONBUK, "익산시"),
    JEONGEUP(RegionTop.JEONBUK, "정읍시"),
    NAMWON(RegionTop.JEONBUK, "남원시"),
    GIMJE(RegionTop.JEONBUK, "김제시"),
    WANJU(RegionTop.JEONBUK, "완주군"),
    JANGSU(RegionTop.JEONBUK, "장수군"),
    MUJU(RegionTop.JEONBUK, "무주군"),
    JINAN(RegionTop.JEONBUK, "진안군"),
    IMSIL(RegionTop.JEONBUK, "임실군"),
    SUNCHANG(RegionTop.JEONBUK, "순창군"),
    GOCHANG(RegionTop.JEONBUK, "고창군"),
    BUAN(RegionTop.JEONBUK, "부안군"),

    // 강원도
    CHUNCHEON(RegionTop.GANGWON, "춘천시"),
    WONJU(RegionTop.GANGWON, "원주시"),
    GANGNEUNG(RegionTop.GANGWON, "강릉시"),
    DONGHAE(RegionTop.GANGWON, "동해시"),
    TAEBAEK(RegionTop.GANGWON, "태백시"),
    SOKCHO(RegionTop.GANGWON, "속초시"),
    SAMCHEOK(RegionTop.GANGWON, "삼척시"),
    HONGCHEON(RegionTop.GANGWON, "홍천군"),
    HOENGSEONG(RegionTop.GANGWON, "횡성군"),
    YEONGWOL(RegionTop.GANGWON, "영월군"),
    PYEONGCHANG(RegionTop.GANGWON, "평창군"),
    JEONGSEON(RegionTop.GANGWON, "정선군"),
    CHEORWON(RegionTop.GANGWON, "철원군"),
    HWACHEON(RegionTop.GANGWON, "화천군"),
    YANGGU(RegionTop.GANGWON, "양구군"),
    INJE(RegionTop.GANGWON, "인제군"),
    GOSEONG_GANGWON(RegionTop.GANGWON, "고성군"),
    YANGYANG(RegionTop.GANGWON, "양양군"),

    // 대전
    DAEJEON_CITY(RegionTop.DAEJEON, "대전시"),
    DAEJEON_DONGGU(RegionTop.DAEJEON, "동구"),
    DAEJEON_SEOGU(RegionTop.DAEJEON, "서구"),
    DAEJEON_YUSEONGGU(RegionTop.DAEJEON, "유성구"),
    DAEJEON_JUNGGU(RegionTop.DAEJEON, "중구"),
    DAEJEON_DAEDEOKGU(RegionTop.DAEJEON, "대덕구"),

    // 충남 (충청남도)
    CHEONAN(RegionTop.CHUNGNAM, "천안시"),
    NONGSAN(RegionTop.CHUNGNAM, "논산시"),
    ASAN(RegionTop.CHUNGNAM, "아산시"),
    DANGJIN(RegionTop.CHUNGNAM, "당진시"),
    SEOSAN(RegionTop.CHUNGNAM, "서산시"),
    HONGSEONG(RegionTop.CHUNGNAM, "홍성군"),
    YESAN(RegionTop.CHUNGNAM, "예산군"),
    BOERYEONG(RegionTop.CHUNGNAM, "보령시"),
    BUYEOGUN(RegionTop.CHUNGNAM, "부여군"),
    CHEONGYANG(RegionTop.CHUNGNAM, "청양군"),
    GONGJU(RegionTop.CHUNGNAM, "공주시"),
    GEUMSAN(RegionTop.CHUNGNAM, "금산군"),
    SEACHEON(RegionTop.CHUNGNAM, "서천군"),
    TAEAN(RegionTop.CHUNGNAM, "태안군"),

    // 충북 (충청북도)
    CHEONGJU(RegionTop.CHUNGBUK, "청주시"),
    CHUNGJU(RegionTop.CHUNGBUK, "충주시"),
    JECHUN(RegionTop.CHUNGBUK, "제천시"),
    BOEUN(RegionTop.CHUNGBUK, "보은군"),
    OKCHEON(RegionTop.CHUNGBUK, "옥천군"),
    YEONGDONG(RegionTop.CHUNGBUK, "영동군"),
    JINCHUN(RegionTop.CHUNGBUK, "진천군"),
    GOESAN(RegionTop.CHUNGBUK, "괴산군"),
    EUMSEONG(RegionTop.CHUNGBUK, "음성군"),
    DANYANG(RegionTop.CHUNGBUK, "단양군"),

    // 제주
    JEJU_CITY(RegionTop.JEJU, "제주시"),
    SEOGWIPO(RegionTop.JEJU, "서귀포시");



    private final RegionTop regionTop;

    private final String regionBottomName;


    RegionBottom(RegionTop regionTop, String regionBottomName) {
        this.regionTop = regionTop;
        this.regionBottomName = regionBottomName;
    }

    public String getName() {
        return name();
    }
}
