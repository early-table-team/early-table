package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class StoreListResponseDto {

    private final Long storeId;

    private final String storeName;

    private final String storeTel;

    private final String storeContent;

    private final String storeAddress;

    private final StoreCategory storeCategory;

    private final String presentMenu;

    private final String ownerName;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final StoreStatus storeStatus;

    private final Map<Integer, String> storeImageUrlMap;

    private final RegionTop regionTop;

    private final RegionBottom regionBottom;


    public StoreListResponseDto(Long storeId, String storeName, String storeTel, String storeContent,
                                String storeAddress, StoreCategory storeCategory, String ownerName,
                                LocalDateTime createdAt, LocalDateTime modifiedAt,
                                StoreStatus storeStatus, String presentMenu, Map<Integer, String> storeImageUrlMap, RegionTop regionTop, RegionBottom regionBottom) {

        this.storeId = storeId;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContent = storeContent;
        this.storeAddress = storeAddress;
        this.storeCategory = storeCategory;
        this.ownerName = ownerName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.storeStatus = storeStatus;
        this.presentMenu = presentMenu;
        this.storeImageUrlMap = storeImageUrlMap;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
    }


    public static StoreListResponseDto toDto(Store store) {

        Map<Integer, String> imageFileUrlMap = new HashMap<>();
        for(FileDetail fileDetail : store.getFile().getFileDetailList()){
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUrl());
        }

        return new StoreListResponseDto(
                store.getStoreId(), store.getStoreName(),
                store.getStoreTel(), store.getStoreContents(),
                store.getStoreAddress(), store.getStoreCategory(),
                store.getUser().getNickName(),
                store.getCreatedAt(), store.getModifiedAt(),
                store.getStoreStatus(),
                // 대표 메뉴
                store.getMenuList().stream()
                        .filter(menu -> menu.getMenuStatus().equals(MenuStatus.RECOMMENDED)).findFirst()
                        .map(Menu::getMenuName).orElse(null),
                // 이미지 url
                imageFileUrlMap,
                store.getRegionTop(),
                store.getRegionBottom()

        );
    }
}
