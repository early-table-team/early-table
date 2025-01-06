package com.gotcha.earlytable.global.error;

import com.gotcha.earlytable.global.dto.CommonResponseBody;
import com.gotcha.earlytable.global.error.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 예외 처리.
     *
     * @param ce CustomException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<String>>}
     */
    @ExceptionHandler(value = {CustomException.class, BadCredentialsException.class, UnauthorizedException.class,
                                ForbiddenException.class, NotFoundException.class, ConflictException.class})
    public ResponseEntity<CommonResponseBody<String>> handleCustomException(CustomException ce) {

        return ResponseEntity
                .status(ce.getErrorCode().getHttpStatus())
                .body(new CommonResponseBody<>(ce.getErrorCode().getDetail()));
    }

    /**
     * Validation 예외 처리.
     *
     * @param e HandlerMethodValidationException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<String>>}
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<CommonResponseBody<String>> handleMethodValidationExceptions(
            HandlerMethodValidationException e) {
        String message = e.getParameterValidationResults().get(0).getResolvableErrors().get(0)
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseBody<>(message));
    }

    /**
     * Validation 예외 처리.
     *
     * @param e MethodArgumentNotValidException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<String>>}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseBody<String>> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseBody<>(message));
    }

    /**
     * Security와 관련된 AuthenticationException 예외 처리.
     *
     * @param e AuthenticationException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<Void>>}
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAuthException(
            AuthenticationException e) {
        HttpStatus statusCode = e instanceof BadCredentialsException
                ? HttpStatus.FORBIDDEN
                : HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(statusCode)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    /**
     * Security와 관련된 AccessDeniedException 예외 처리.
     *
     * @param e AccessDeniedException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<Void>>}
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAccessDeniedException(
            AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    /**
     * Security와 관련된 AuthorizationDeniedException 예외 처리.
     *
     * @param e AuthorizationDeniedException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<Void>>}
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAuthorizationDeniedException(
            AuthorizationDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    /**
     * JWT와 관련된 JwtException 예외 처리.
     *
     * @param e JwtException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<Void>>}
     */
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleJwtException(JwtException e) {
        HttpStatus httpStatus = e instanceof ExpiredJwtException
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.FORBIDDEN;

        return ResponseEntity
                .status(httpStatus)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    /**
     * ResponseStatusException 예외 처리.
     *
     * @param e ResponseStatusException 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<Void>>}
     */
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleResponseStatusExceptions(
            ResponseStatusException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    /**
     * 그외의 예외 처리.
     *
     * @param e 예외 인스턴스
     * @return {@code ResponseEntity<CommonResponseBody<Void>>}
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleOtherExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseBody<>(e.getMessage()));
    }
}
