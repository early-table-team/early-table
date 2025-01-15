package com.gotcha.earlytable.domain.pendingstore.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PendingStoreRequestDto {

    @NotBlank
    private final String storeName;

    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 핸드폰 번호를 입력하세요.")
    private final String storeTel;

    @NotBlank
    private final String storeAddress;

    @NotBlank
    private final String storeContents;

    @NotNull
    @Enumerated(EnumType.STRING)
    private final RegionTop regionTop;

    @NotNull
    @Enumerated(EnumType.STRING)
    private final RegionBottom regionBottom;

    @NotNull
    @Enumerated(EnumType.STRING)
    private final StoreCategory storeCategory;

    private final List<MultipartFile> storeImageList;


    public PendingStoreRequestDto(String storeName, String storeTel, String storeAddress, String storeContents,
                                  RegionTop regionTop, RegionBottom regionBottom,
                                  StoreCategory storeCategory, List<MultipartFile> storeImageList) {

        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeAddress = storeAddress;
        this.storeContents = storeContents;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
        this.storeCategory = storeCategory;
        this.storeImageList = storeImageList;
    }
}
