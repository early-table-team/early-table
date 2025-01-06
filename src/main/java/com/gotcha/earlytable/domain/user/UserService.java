package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.dto.JwtAuthResponse;
import com.gotcha.earlytable.domain.user.dto.UserLoginRequestDto;
import com.gotcha.earlytable.domain.user.dto.UserRegisterRequestDto;
import com.gotcha.earlytable.domain.user.dto.UserResponseDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.config.PasswordEncoder;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.CustomException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import com.gotcha.earlytable.global.util.AuthenticationScheme;
import com.gotcha.earlytable.global.util.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 회원가입 기능
     * @param requestDto
     * @return UserResponseDto
     */
    @Transactional
    public UserResponseDto registerUser(UserRegisterRequestDto requestDto) {

        // 이메일 중복 검사
        if(userRepository.existsUserByEmail(requestDto.getEmail())){
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // User 생성
        User user = User.toEntity(requestDto, encodedPassword);

        User savedUser = userRepository.save(user);

        return UserResponseDto.toDto(savedUser);
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
}
