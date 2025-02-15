package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.friend.FriendRepository;
import com.gotcha.earlytable.domain.user.dto.*;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.config.PasswordEncoder;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import com.gotcha.earlytable.global.util.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileDetailService fileDetailService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final FriendRepository friendRepository;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository, FileDetailService fileDetailService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider, FileService fileService, FriendRepository friendRepository, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.fileDetailService = fileDetailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.fileService = fileService;
        this.friendRepository = friendRepository;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * 회원가입 기능
     *
     * @param requestDto
     * @return UserResponseDto
     */
    @Transactional
    public UserResponseDto registerUser(UserRegisterRequestDto requestDto) {

        // 이메일 중복 검사
        if (userRepository.existsUserByEmail(requestDto.getEmail())) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        if (userRepository.existsUserByPhone(requestDto.getPhone())) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }


        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 파일 생성
        File file = fileService.createFile();

        // User 생성 및 저장
        User user = User.toEntity(requestDto, encodedPassword, file);
        User savedUser = userRepository.save(user);

        String imageUrl = null;
        if (requestDto.getProfileImage() != null && !requestDto.getProfileImage().isEmpty()) {
            // 프로필 이미지 파일 저장
            imageUrl = fileDetailService.createImageFile(requestDto.getProfileImage(), file);
        }

        return UserResponseDto.toDto(savedUser, imageUrl);
    }

    /**
     * 로그인 가능
     *
     * @param requestDto
     * @return JwtAuthResponse
     */
    public String loginUser(UserLoginRequestDto requestDto) {

        User findUser = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

        // 패스워드 일치 여부 검사
        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        // 사용자 인증 후 인증 객체를 저장
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );

        // 시큐리티 컨텍스트 홀더에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtProvider.generateAccessToken(requestDto.getEmail());
    }

    /**
     * refresh Token 을 쿠키에 담기
     *
     * @return Cookie
     */
    public String createCookie(String email) {

        String cookieName = "refreshToken";
        String cookieValue = jwtProvider.generateRefreshToken(email); // 쿠키벨류엔 글자제한이 이써, 벨류로 만들어담아준다.

        // refreshToken db 저장
        refreshTokenService.saveRefreshToken(email, cookieValue);

        // Set-Cookie 헤더를 직접 추가 (SameSite=None; Secure 포함)
        return String.format(
                "%s=%s; Path=/; HttpOnly; Secure; Max-Age=%d; SameSite=None",
                cookieName, cookieValue, 60 * 60 * 24
        );

    }


    /**
     * refreshToken 확인 및 accessToken 재발급
     *
     * @param email
     * @return accessToken
     */
    public String refresh(String email, String refreshToken) {

        if(!refreshTokenService.validateRefreshToken(email, refreshToken)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        return jwtProvider.generateAccessToken(email);
    }

    /**
     * 유저 단건 조회(본인) 메서드
     *
     * @param user
     * @return UserResponseDto
     */
    public UserResponseDto getUser(User user) {

        String imageUrl = user.getFile().getFileDetailList().stream()
                .filter(file -> file.getFileStatus().equals(FileStatus.REPRESENTATIVE)).findFirst()
                .map(FileDetail::getFileUrl)
                .orElse(null);

        return UserResponseDto.toDto(user, imageUrl);
    }

    /**
     * 유저 단건 조회(타인) 메서드
     *
     * @param user
     * @return UserResponseDto
     */
    public OtherUserResponseDto getOtherUser(User user, Long otherUserId) {

        String relationship = "other";

        if (user.getId().equals(otherUserId)) {
            relationship = "mine";
        }

        boolean isFriend = friendRepository.findBySendUserId(user.getId()).stream()
                .anyMatch(friend -> friend.getReceivedUser().getId().equals(otherUserId));
        if (isFriend) {
            relationship = "friend";
        }

        User otherUser = userRepository.findByIdOrElseThrow(otherUserId);

        String imageUrl = otherUser.getFile().getFileDetailList().stream()
                .filter(file -> file.getFileStatus().equals(FileStatus.REPRESENTATIVE)).findFirst()
                .map(FileDetail::getFileUrl)
                .orElse(null);

        return OtherUserResponseDto.toDto(otherUser, imageUrl, relationship);
    }

    /**
     * 유저 탈퇴 메서드
     *
     * @param requestDto
     * @param user
     */
    @Transactional
    public void deleteUser(UserDeleteRequestDto requestDto, User user) {

        // 비밀번호 값이 일치하지 않는경우 BAD_REQUEST 발생
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        //둘다 원만한 경우 유저 삭제 후 200 OK  발생
        user.asDelete();
        userRepository.save(user);
    }

    /**
     * 유저 정보 변경 메서드
     *
     * @param user
     * @param requestDto
     * @return UserResponseDto
     */
    @Transactional
    public UserResponseDto updateUser(User user, UserUpdateRequestDto requestDto) {

        // 정보 수정 및 저장
        user.updateUser(requestDto);
        User savedUser = userRepository.save(user);

        // 기존 프로필 이미지 url 가져오기
        String imageUrl = user.getFile().getFileDetailList().stream()
                .findAny().map(FileDetail::getFileUrl).orElse(null);

        if (requestDto.getProfileImage() != null && !requestDto.getProfileImage().isEmpty()) {

            // 기존 이미지 제거
            user.getFile().getFileDetailList().stream()
                    .findAny().ifPresent(fileDetail -> fileDetailService.deleteImageFile(fileDetail.getFileUrl()));

            // 새로 생성
            imageUrl = fileDetailService.createImageFile(requestDto.getProfileImage(), user.getFile());
        }

        return UserResponseDto.toDto(savedUser, imageUrl);
    }

    /**
     * 비밀번호 변경 메서드
     *
     * @param user
     * @param requestDto
     */
    @Transactional
    public void updateUserPW(User user, UserPWRequestDto requestDto) {

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 기존 패스워드를 알고 있는 지 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 새로운 패스워드가 기존과 동일한 지 확인
        if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 정보 수정
        user.updateUserPW(encodedPassword);

        userRepository.save(user);
    }

    /**
     * 마이페이지 내 유저 예약 현황 카운트 API
     */
    public UserReservationCountResponseDto getUserReservationCount(Long userId) {
        long reservationCount = userRepository.countPendingReservationsByUserNative("PENDING", userId);
        long waitingCount = userRepository.countPendingWaitingsByUserNative("PENDING", userId);


        return new UserReservationCountResponseDto(reservationCount, waitingCount);
    }

    /**
     * 타인 유저 검색 조회 API
     * @param userSearchRequestDto
     * @return UserSearchResponseDto
     */
    public List<UserSearchResponseDto> searchUser(UserSearchRequestDto userSearchRequestDto) {
        return userRepository.searchUserQuery(userSearchRequestDto);
    }
}
