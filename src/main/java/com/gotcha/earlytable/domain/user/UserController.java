package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.dto.JwtAuthResponse;
import com.gotcha.earlytable.domain.user.dto.UserLoginRequestDto;
import com.gotcha.earlytable.domain.user.dto.UserRegisterRequestDto;
import com.gotcha.earlytable.domain.user.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    /**
     * 회원가입
     * @param requestDto
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser (@Valid @RequestBody UserRegisterRequestDto requestDto) {

        UserResponseDto registerUser = userService.registerUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }

    /**
     * 로그인 기능
     * @param requestDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> loginUser (@Valid @RequestBody UserLoginRequestDto requestDto) {

        JwtAuthResponse jwtAuthResponse = userService.loginUser(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(jwtAuthResponse);
    }

    /**
     * 로그아웃 기능
     * @param request
     * @param response
     * @param authentication
     * @return
     * @throws UsernameNotFoundException
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws UsernameNotFoundException {

        // 인증 정보가 있다면 로그아웃 처리.
        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            request.getSession(false).invalidate();

            return ResponseEntity.ok("로그아웃 성공.");
        }

        // 인증 정보가 없다면 인증되지 않았기 때문에 로그인 필요.
        throw new UsernameNotFoundException("로그인이 먼저 필요합니다.");

    }

}
