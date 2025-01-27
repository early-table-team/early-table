package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.dto.*;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import com.gotcha.earlytable.global.util.AuthenticationScheme;
import com.gotcha.earlytable.global.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
import java.util.List;
>>>>>>> 8f9df7bd3e9e816d13389c0399e5fd247efe0a54

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
<<<<<<< HEAD
    private final SearchService searchService;

    public UserController(UserService userService, SearchService searchService) {

        this.userService = userService;
        this.searchService = searchService;
=======
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    public UserController(UserService userService, RefreshTokenService refreshTokenService, JwtProvider jwtProvider) {

        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
>>>>>>> 8f9df7bd3e9e816d13389c0399e5fd247efe0a54
    }

    /**
     * 회원가입 API
     *
     * @param requestDto
     * @return ResponseEntity<UserResponseDto>
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @ModelAttribute UserRegisterRequestDto requestDto) {
        UserResponseDto registerUser = userService.registerUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }

    /**
     * 로그인 기능 API
     *
     * @param requestDto
     * @return ResponseEntity<JwtAuthResponse>
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> loginUser(@Valid @RequestBody UserLoginRequestDto requestDto,
                                                     HttpServletResponse response) {

        String accessToken = userService.loginUser(requestDto);

        response.addHeader("Authorization", accessToken);
        response.addCookie(userService.craeteCookie(requestDto.getEmail()));


        return ResponseEntity.status(HttpStatus.OK).body(new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken));
    }


    /**
     * 로그아웃 기능 API
     *
     * @param request
     * @param response
     * @param authentication
     * @return ResponseEntity<String>
     * @throws UsernameNotFoundException
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws UsernameNotFoundException {

        // 인증 정보가 있다면 로그아웃 처리.
        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            refreshTokenService.deleteRefreshToken(authentication.getName());

            return ResponseEntity.ok("로그아웃 성공.");
        }

        // 인증 정보가 없다면 인증되지 않았기 때문에 로그인 필요.
        throw new UsernameNotFoundException("로그인이 먼저 필요합니다.");

    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken,
                                          HttpServletResponse response) {
        if (refreshToken == null || !jwtProvider.validToken(refreshToken)) {
            return null;
        }
        String email = jwtProvider.getUsername(refreshToken);

        String newAccessToken = userService.refresh(email, refreshToken);

        if(newAccessToken != null) {
            response.addHeader("Authorization", newAccessToken);
            response.addCookie(userService.craeteCookie(email));
            return ResponseEntity.ok(new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), newAccessToken));
        }

        throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }

    /**
     * 유저 단건 조회 API
     *
     * @param userDetails
     * @return ResponseEntity<UserResponseDto>
     */
    @GetMapping("/mine")
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserResponseDto userResponseDto = userService.getUser(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    /**
     * 타인 유저 단건 조회 API
     *
     * @param userDetails
     * @return ResponseEntity<UserResponseDto>
     */
    @GetMapping("/{userId}")
    public ResponseEntity<OtherUserResponseDto> getOtherUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable Long userId) {

        OtherUserResponseDto responseDto = userService.getOtherUser(userDetails.getUser(), userId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 타인 유저 검색 조회 API
     * (OWNER, ADMIN 검색결과 제외)
     * @param userSearchRequestDto
     * @return ResponseEntity<List<UserSearchResponseDto>>
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponseDto>> searchUser(@ModelAttribute UserSearchRequestDto userSearchRequestDto) {

        List<UserSearchResponseDto> responseDto = userService.searchUser(userSearchRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 유저 정보 수정 API
     *
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<UserResponseDto>
     */
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@Valid @ModelAttribute UserUpdateRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserResponseDto userResponseDto = userService.updateUser(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    /**
     * 유저 비밀번호 변경 API
     *
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @PatchMapping("/pw")
    public ResponseEntity<String> updateUserPW(@Valid @RequestBody UserPWRequestDto requestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.updateUserPW(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 수정이 완료되었습니다.");
    }

    /**
     * 유저 삭제 API
     *
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody UserDeleteRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.deleteUser(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 마이페이지 내 예약 현황 카운트 API
     * @param userDetails
     * @return UserReservationCountResponseDto
     */
    @GetMapping("/count")
    public ResponseEntity<UserReservationCountResponseDto> getUserReservationCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserReservationCountResponseDto userReservationCountResponseDto = userService.getUserReservationCount(userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body(userReservationCountResponseDto);
    }


    /**
     * 서칭 페이지 init api
     * @return
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/search/init")
    public Map<String, Object> getInit(){
        Map<String, Object> response = new HashMap<>();

        response.put("regions", searchService.getRegions());

        response.put("storeCategories", searchService.getStoreCategories());

        response.put("allergyStuff", searchService.getAllergyCategoriesAndStuff());



        return response;

    }
}
