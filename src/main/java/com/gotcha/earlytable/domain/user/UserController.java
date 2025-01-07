package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.dto.*;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response, Authentication authentication)
            throws UsernameNotFoundException {

        // 인증 정보가 있다면 로그아웃 처리.
        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            request.getSession(false).invalidate();

            return ResponseEntity.ok("로그아웃 성공.");
        }

        // 인증 정보가 없다면 인증되지 않았기 때문에 로그인 필요.
        throw new UsernameNotFoundException("로그인이 먼저 필요합니다.");

    }

    /**
     * 유저 단건 조회
     * @param userDetails
     * @return ResponseEntity<UserResponseDto>
     */
    @GetMapping("/users")
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails){

        UserResponseDto userResponseDto = userService.getUser(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    /**
     *  유저 삭제
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUser(@RequestBody UserDeleteRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        userService.deleteUser(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴가 완료되었습니다.");
    }


}
