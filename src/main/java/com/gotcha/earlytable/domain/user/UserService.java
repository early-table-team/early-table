package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.user.dto.*;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.config.PasswordEncoder;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import com.gotcha.earlytable.global.util.AuthenticationScheme;
import com.gotcha.earlytable.global.util.JwtProvider;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileDetailService fileDetailService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, FileDetailService fileDetailService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider, FileService fileService, ResourceLoader resourceLoader) {
        this.userRepository = userRepository;
        this.fileDetailService = fileDetailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.fileService = fileService;
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
        if(userRepository.existsUserByEmail(requestDto.getEmail())) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 파일 생성
        File file = fileService.createFile();

        String imageUrl = null;
        if(!requestDto.getProfileImage().isEmpty()) {
            // 프로필 이미지 파일 저장
            imageUrl = fileDetailService.createImageFile(requestDto.getProfileImage(), file);
        }

        // User 생성
        User user = User.toEntity(requestDto, encodedPassword, file);

        User savedUser = userRepository.save(user);

        return UserResponseDto.toDto(savedUser, imageUrl);
    }

    /**
     * 로그인 가능
     * @param requestDto
     * @return JwtAuthResponse
     */
    public JwtAuthResponse loginUser(UserLoginRequestDto requestDto) {

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

        // 토큰 생성
        String accessToken = this.jwtProvider.generateToken(authentication);

        return new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    /**
     * 유저 단건 조회(본인) 메서드
     *
     * @param user
     * @return UserResponseDto
     */
    public UserResponseDto getUser(User user){

        String imageUrl = user.getFile().getFileDetailList().stream()
                .filter(file -> file.getFileStatus().equals(FileStatus.REPRESENTATIVE)).findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND))
                .getFileUrl();

        return UserResponseDto.toDto(user, imageUrl);
    }

    /**
     *  유저 탈퇴 메서드
     *
     * @param requestDto
     * @param user
     */
    @Transactional
    public void deleteUser(UserDeleteRequestDto requestDto, User user){

        // 비밀번호 값이 일치하지 않는경우 BAD_REQUEST 발생
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        //둘다 원만한 경우 유저 삭제 후 200 OK  발생
        user.asDelete();
        userRepository.save(user);
    }

    /**
     *  유저 정보 변경 메서드
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

        if (!requestDto.getProfileImage().isEmpty()) {

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
        // 정보 수정
        user.updateUserPW(encodedPassword);

        userRepository.save(user);
    }
}
