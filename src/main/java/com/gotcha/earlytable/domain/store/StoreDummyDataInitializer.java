package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class StoreDummyDataInitializer {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    private final Random random = new Random();
    private final Faker faker = new Faker(Locale.KOREAN);

    private static final int STORE_COUNT = 10_000;

    // @PostConstruct
    @Transactional
    public void init() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new IllegalStateException("User가 존재하지 않습니다.");
        }

        List<Store> stores = new ArrayList<>();

        for (int i = 0; i < STORE_COUNT; i++) {
            stores.add(createRandomStore(users));

            if (i % 10_000 == 0) { // 10,000개씩 배치 저장
                storeRepository.saveAll(stores);
                stores.clear();
                System.out.println(i + " 개의 데이터를 저장 완료...");
            }
        }

        if (!stores.isEmpty()) {
            storeRepository.saveAll(stores);
        }

        System.out.println("총 " + STORE_COUNT + " 개의 더미 데이터 저장 완료!");
    }

    private Store createRandomStore(List<User> users) {
        return new Store(
                faker.company().name(), // 랜덤 상점명
                faker.phoneNumber().phoneNumber(), // 랜덤 전화번호
                faker.lorem().sentence(), // 랜덤 설명
                faker.address().fullAddress(), // 랜덤 주소
                randomEnum(StoreStatus.class), // 랜덤 StoreStatus
                randomEnum(StoreCategory.class), // 랜덤 StoreCategory
                randomEnum(RegionTop.class), // 랜덤 RegionTop
                randomEnum(RegionBottom.class), // 랜덤 RegionBottom
                getRandomElement(users), // 랜덤 User
                fileService.createFile() // New File
        );
    }

    private <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[random.nextInt(enumConstants.length)];
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
