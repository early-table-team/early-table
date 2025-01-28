package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.allergy.AllergyCategoryRepository;
import com.gotcha.earlytable.domain.allergy.AllergyRepository;
import com.gotcha.earlytable.domain.allergy.AllergyStuffRepository;
import com.gotcha.earlytable.domain.allergy.entity.AllergyStuff;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final AllergyCategoryRepository allergyCategoryRepository;
    private final AllergyStuffRepository allergyStuffRepository;

    public SearchService(AllergyCategoryRepository allergyCategoryRepository, AllergyStuffRepository allergyStuffRepository) {
        this.allergyCategoryRepository = allergyCategoryRepository;
        this.allergyStuffRepository = allergyStuffRepository;
    }


    /**
     *  지역정보 가져오는 메서드
     * @return
     */
    public List<Map<String, String>> getRegions() {
        // 지역 정보를 저장할 리스트
        List<Map<String, String>> regions = new ArrayList<>();

        // 상위 지역을 기준으로 하위 지역을 매핑
        for (RegionTop topRegion : RegionTop.values()) {
            // 하위 지역들을 필터링하여 상위 지역에 속하는 하위 지역들만 가져오기
            List<String> bottomRegions = Arrays.stream(RegionBottom.values())
                    .filter(region -> region.getRegionTop() == topRegion)  // 상위 지역이 일치하는 하위 지역만
                    .map(region -> region.getName() + " (" + region.getRegionBottomName() + ")")  // 이넘 값과 한국어 이름을 쌍으로
                    .collect(Collectors.toList());

            // 상위 지역과 그에 해당하는 하위 지역들을 Map 형태로 추가
            regions.add(Map.of(
                    "topRegion", topRegion.name() + " (" + topRegion.getName() + ")",  // 이넘 값과 한국어 이름을 쌍으로
                    "bottomRegions", String.join(", ", bottomRegions)  // 하위 지역들을 쉼표로 구분한 문자열로 합침
            ));
        }

        return regions;
    }

    /**
     * 가게 카테고리 반환 메서드
     * @return
     */
    public List<Map<String, String>> getStoreCategories(){
        return Arrays.stream(StoreCategory.values())
                .map(category -> Map.of("code", category.name(), "name", category.getCategoryName()))
                .collect(Collectors.toList());
    }


    /**
     * 알러지 정보 가져오는 메서드
     * @return
     */
    public List<Map<String, String>> getAllergyCategoriesAndStuff() {
        // 알러지 카테고리와 원재료들을 가져옵니다.
        List<Map<String, String>> allergyCategoriesAndStuff = new ArrayList<>();

        // 각 알러지 카테고리별로 원재료들을 묶습니다.
        List<Map<String, String>> allergyCategories = allergyCategoryRepository.findAll().stream()
                .map(category -> Map.of(
                        "id", String.valueOf(category.getAllergyCategoryId()),
                        "name", category.getAllergyCategory()
                ))
                .collect(Collectors.toList());

        // 알러지 카테고리마다 해당하는 원재료들을 묶어 반환합니다.
        for (Map<String, String> category : allergyCategories) {
            String categoryId = category.get("id");
            String categoryName = category.get("name");

            // 해당 카테고리에 속하는 원재료들 필터링
            List<String> allergyStuffList = allergyStuffRepository.findAll().stream()
                    .filter(stuff -> stuff.getAllergyCategory().getAllergyCategoryId().toString().equals(categoryId))
                    .map(stuff -> stuff.getAllergyStuff())
                    .collect(Collectors.toList());

            allergyCategoriesAndStuff.add(Map.of(
                    "Category", categoryName,
                    "Stuff", String.join(", ", allergyStuffList)
            ));
        }

        return allergyCategoriesAndStuff;
    }

}
