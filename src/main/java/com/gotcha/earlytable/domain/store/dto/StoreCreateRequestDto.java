package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class StoreCreateRequestDto {

    @NotBlank
    private final String storeName;

    @NotBlank
    private final String storeTel;

    @NotBlank
    private final String storeAddress;

    @NotBlank
    private final String storeContents;

    @NotBlank
    private final String regionTop;

    @NotBlank
    private final String regionBottom;


    @Enumerated(EnumType.STRING)
    private final StoreCategory storecategory;

    private final List<MultipartFile> storeImageList;


    public StoreCreateRequestDto(String storeName, String storeTel, String storeAddress, String storeContents,
                                 String regionTop, String regionBottom,
                                 StoreCategory storecategory, List<MultipartFile> storeImageList) {
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeAddress = storeAddress;
        this.storeContents = storeContents;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
        this.storecategory = storecategory;
        this.storeImageList = storeImageList;
    }
}
