package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class StoreUpdateRequestDto {

    private final Long userId;

    private final Long fileId;

    private final String storeName;

    private final String storeTel;

    private final String storeAddress;

    private final String storeContents;

    private final String regionTop;

    private final String regionBottom;

    @Enumerated(EnumType.STRING)
    private final StoreCategory storeCategory;

    private final List<MultipartFile> newStoreImageList;

    private final List<String> fileUrlList;


    public StoreUpdateRequestDto(Long userId, Long fileId, String storeName, String storeTel, String storeAddress,
                                 String storeContents, String regionTop, String regionBottom, StoreCategory storeCategory,
                                 List<MultipartFile> newStoreImageList, List<String> fileUrlList) {

        this.userId = userId;
        this.fileId = fileId;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeAddress = storeAddress;
        this.storeContents = storeContents;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
        this.storeCategory = storeCategory;
        this.newStoreImageList = newStoreImageList;
        this.fileUrlList = fileUrlList;
    }
  
}
