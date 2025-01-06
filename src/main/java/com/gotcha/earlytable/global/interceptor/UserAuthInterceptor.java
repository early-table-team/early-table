package com.gotcha.earlytable.global.interceptor;


import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ForbiddenException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Slf4j
@Component
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            CheckUserAuth checkUserAuth = handlerMethod.getMethodAnnotation(CheckUserAuth.class);

            // 어노테이션이 없는 경우, 모든 사용자 허용
            if (checkUserAuth == null) {
                return true;
            }

            // 인증 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증 정보가 없으면 예러
            if (authentication == null) {
                throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
            }

            // 인증 정보 내의 유저 정보 가져오기
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();

            // 어노테이션 사용 시 입력된 권한 가져오기
            Auth[] requiredAuthorities = checkUserAuth.requiredAuthorities();

            if (!Arrays.asList(requiredAuthorities).contains(user.getAuth())) {
                throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
            }
        }

        return true;
    }
}


