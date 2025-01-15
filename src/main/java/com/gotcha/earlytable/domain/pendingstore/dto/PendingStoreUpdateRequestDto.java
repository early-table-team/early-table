package com.gotcha.earlytable.domain.pendingstore.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PendingStoreUpdateRequestDto {

    private final String storeName;

    @Pattern(regexp = "^\\d{2,3}-\\d{4}-\\d{4}$", message = "유효한 핸드폰 번호를 입력하세요.")
    private final String storeTel;

    private final String storeAddress;

    private final String storeContents;

    @Enumerated(EnumType.STRING)
    private final RegionTop regionTop;

    @Enumerated(EnumType.STRING)
    private final RegionBottom regionBottom;


    @Enumerated(EnumType.STRING)
    private final StoreCategory storeCategory;

    private final List<MultipartFile> newStoreImageList;

    private final List<String> fileUrlList;



    public PendingStoreUpdateRequestDto(String storeName, String storeTel, String storeAddress,
                                        String storeContents, RegionTop regionTop, RegionBottom regionBottom, StoreCategory storeCategory,
                                        List<MultipartFile> newStoreImageList, List<String> fileUrlList) {

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

