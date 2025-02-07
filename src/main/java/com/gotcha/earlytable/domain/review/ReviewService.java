package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.notification.FcmService;
import com.gotcha.earlytable.domain.notification.SseEmitterService;
import com.gotcha.earlytable.domain.reservation.ReservationRepository;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewTotalResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewUpdateRequestDto;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.review.enums.ReviewStatus;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.WaitingRepository;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.NotificationType;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final FileDetailService fileDetailService;
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final SseEmitterService sseEmitterService;
    private final FcmService fcmService;

    public ReviewService(ReviewRepository reviewRepository, StoreRepository storeRepository, FileService fileService,
                         FileDetailService fileDetailService, ReservationRepository reservationRepository,
                         WaitingRepository waitingRepository, SseEmitterService sseEmitterService1, FcmService fcmService) {
        this.reviewRepository = reviewRepository;
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.fileDetailService = fileDetailService;
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.sseEmitterService = sseEmitterService1;
        this.fcmService = fcmService;
    }


    /**
     * 리뷰 생성 서비스 메소드
     */
    @Transactional
    public ReviewResponseDto createReview(Long storeId, User user, ReviewRequestDto reviewRequestDto){

        // 이미 작성한 예약건이면 예외처리
        boolean isExist = reviewRepository
                .existsByTargetIdAndReviewTarget(reviewRequestDto.getTargetId(), reviewRequestDto.getTargetObject());
        if(isExist) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 어떤 예약 및 웨이팅에서 발생된 리뷰 생성인지 확인
        switch (reviewRequestDto.getTargetObject()) {
            case RESERVATION:
                Reservation reservation = reservationRepository.findByIdOrElseThrow(reviewRequestDto.getTargetId());

                if(reservation.getReservationStatus() != ReservationStatus.COMPLETED) {
                    throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
                }

                // 예약 대표자가 본인인지 확인
                boolean isMineForReservation = reservation.getParty().getPartyPeople().stream()
                        .filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE))
                        .anyMatch(partyPeople -> partyPeople.getUser().getId().equals(user.getId()));
                if(!isMineForReservation) {
                    throw new UnauthorizedException(ErrorCode.FORBIDDEN_PERMISSION);
                }
                break;

            case WAITING:
                Waiting waiting = waitingRepository.findByIdOrElseThrow(reviewRequestDto.getTargetId());

                if(waiting.getWaitingStatus() != WaitingStatus.COMPLETED) {
                    throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
                }

                // 웨이팅 대표자가 본인인지 확인
                boolean isMineForWaiting = waiting.getParty().getPartyPeople().stream()
                        .filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE))
                        .anyMatch(partyPeople -> partyPeople.getUser().getId().equals(user.getId()));
                if(!isMineForWaiting) {
                    throw new UnauthorizedException(ErrorCode.FORBIDDEN_PERMISSION);
                }
                break;

            default :
                throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 필요한 객체 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);
        File file = fileService.createFile();

        // 이미지 파일들 저장
        if(reviewRequestDto.getReviewImageList() != null && !reviewRequestDto.getReviewImageList().get(0).isEmpty()) {
            // 프로필 이미지 파일 저장
            fileDetailService.createImageFiles(reviewRequestDto.getReviewImageList(), file);
        }

        // 리뷰 객체 생성
        Review review = new Review(
                reviewRequestDto.getRating(),
                reviewRequestDto.getReviewContent(),
                ReviewStatus.NORMAL,
                store, user, file,
                reviewRequestDto.getTargetObject(), reviewRequestDto.getTargetId()
        );

        // 저장
        Review savedReview = reviewRepository.save(review);

        // 알림 전송
        String message = store.getStoreName() +"의 가게에 리뷰가 달렸습니다.";
        sseEmitterService.send(store.getUser(), message, NotificationType.REVIEW);
        fcmService.sendNotificationByToken("리뷰 등록", message, "", store.getUser());

        return ReviewResponseDto.toDto(savedReview);
    }

    /**
     * 리뷰 수정 서비스 메서드
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto, Long userId) {

        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        //내가 쓴 리뷰인지 확인
        if(!review.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        //리뷰 수정 및 저장
        review.updateReview(reviewUpdateRequestDto);
        Review updatedReview = reviewRepository.save(review);

        // 이미지 수정
        if(reviewUpdateRequestDto.getFileUrlList() != null && !reviewUpdateRequestDto.getFileUrlList().isEmpty()) {

            fileDetailService.updateFileDetail(reviewUpdateRequestDto.getNewReviewImageList(), reviewUpdateRequestDto.getFileUrlList(), review.getFile());
        }

        return ReviewResponseDto.toDto(updatedReview);
    }

    /**
     * 가게 전체 리뷰 조회 서비스 메서드
     */
    public List<StoreReviewResponseDto> getStoreReviews(Long storeId) {

        storeRepository.findByIdOrElseThrow(storeId);

        List<Review> reviews = reviewRepository.findAllByStoreStoreIdAndReviewStatus(storeId,ReviewStatus.NORMAL);

        return reviews.stream().map(StoreReviewResponseDto::toDto).toList();
    }

    /**
     * 나의 전체 리뷰 조회 서비스 메서드
     */
    public List<ReviewResponseDto> getMyReviews(User user) {

        List<Review> reviews = reviewRepository.findAllByUserIdAndReviewStatus(user.getId(),ReviewStatus.NORMAL);

        return reviews.stream().map(ReviewResponseDto::toDto).toList();
    }

    /**
     * 가게 리뷰 평점 조회 서비스 메서드
     */
    public ReviewTotalResponseDto getStoreReviewTotal(Long storeId) {

        storeRepository.findByIdOrElseThrow(storeId);

        Map<String, Number> result = reviewRepository.findStatisticsByStoreId(storeId, ReviewStatus.NORMAL);

        // 리뷰가 존재하지 않으면
        for(Number number : result.values()) {
            if(number == null) {
                return new ReviewTotalResponseDto(
                        0,0,0,0,0,0, (double) 0
                );
            }
        }

        return new ReviewTotalResponseDto(
                (result.get("ratingStat1")).intValue(),
                (result.get("ratingStat2")).intValue(),
                (result.get("ratingStat3")).intValue(),
                (result.get("ratingStat4")).intValue(),
                (result.get("ratingStat5")).intValue(),
                (result.get("countTotal")).intValue(),
                (Double) result.get("ratingAverage")
        );
    }

    /**
     * 리뷰 삭제 서비스 메서드
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {

        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        // 리뷰가 내가 작성한게 맞나 확인하기
        if(!review.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        //탈퇴 상태로 업데이트
        review.updateReviewStatusToDeleted();
    }
}
