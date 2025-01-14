package com.gotcha.earlytable.domain.pendingstore.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PendingStoreUpdateRequestDto {

    @NotNull
    private final Long userId;

    @NotNull
    private final Long fileId;

    @NotBlank
    private final String storeName;

    @NotBlank
    private final String storeTel;

    @NotBlank
    private final String storeAddress;

    @NotBlank
    private final String storeContents;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private final RegionTop regionTop;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private final RegionBottom regionBottom;


    @Enumerated(EnumType.STRING)
    private final StoreCategory storeCategory;

    private final List<MultipartFile> newStoreImageList;

    private final List<String> fileUrlList;


    public PendingStoreUpdateRequestDto(Long userId, Long fileId, String storeName, String storeTel, String storeAddress,
                                        String storeContents, RegionTop regionTop, RegionBottom regionBottom, StoreCategory storeCategory,
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
