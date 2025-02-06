package com.gotcha.earlytable;

import com.gotcha.earlytable.domain.file.FileRepository;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.reservation.ReservationMenuRepository;
import com.gotcha.earlytable.domain.reservation.ReservationService;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateRequestDto;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.UserService;
import com.gotcha.earlytable.domain.user.dto.UserRegisterRequestDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Auth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationService reservationService;

    private User user;

    private int totalRequests = 0;
    private int successfulRequests = 0;


    @BeforeEach
    void beforeEach() {
        System.out.println("테스트 시작!-------------------");
        user = userRepository.findByIdOrElseThrow(1L);

    }

    @AfterEach
    void afterEach() {
        System.out.println("테스트 종료!--------------------");
        System.out.println("총 요청 수: " + totalRequests);
        System.out.println("성공적인 요청 수: " + successfulRequests);
    }


    @Test
    void reservation_create(){
        try {
            String dateString = "2025-02-05 11:00:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateString, formatter);

            ExecutorService executorService = Executors.newFixedThreadPool(100);  // 최대 100개 스레드 사용
            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                tasks.add(() -> {
                    totalRequests++;
                    ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto(
                            date,   // 동일한 날짜와 시간
                            3,      // 동일한 인원수
                            List.of(
                                    new HashMap<String, Long>() {{
                                        put("menuId", 11L);  // 동일한 메뉴 ID
                                        put("menuCount", 2L); // 동일한 메뉴 수
                                    }}
                            )
                    );
                    reservationService.createReservation(4L, requestDto, user);  // 예약 생성

                    successfulRequests++;
                    return null;
                });
            }
            List<Future<Void>> futures = executorService.invokeAll(tasks);

            for (Future<Void> future : futures) {
                future.get();
            }

            System.out.println("===============================");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }





}
