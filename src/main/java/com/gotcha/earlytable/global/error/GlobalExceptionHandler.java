package com.gotcha.earlytable.global.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.gotcha.earlytable.global.dto.CommonResponseBody;
import com.gotcha.earlytable.global.error.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
     * 파라미터 변환 실패 오류 처리
     *
     * @param ex
     * @return {@code ResponseEntity<CommonResponseBody<String>>}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponseBody<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseBody<>("잘못된 타입입니다." + ex.getLocalizedMessage()));
    }


    /**
     * 데이터 입력 제약 조건 위반 처리
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponseBody<String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseBody<>("데이터 제약 조건 위반하였습니다." + ex.getLocalizedMessage()));
    }

    /**
     * JSON 파싱 오류 처리 1
     *
     * @param ex
     * @return {@code ResponseEntity<CommonResponseBody<String>>}
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<CommonResponseBody<String>> handleInvalidFormatException(InvalidFormatException ex) {
        String fieldName = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .findFirst()
                .orElse("Unknown field");

        String errorMessage = String.format("잘못된 타입입니다. '%s': %s", fieldName, ex.getValue());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseBody<>(errorMessage));
    }

    /**
     * JSON 파싱 오류 처리 2
     *
     * @param ex
     * @return {@code ResponseEntity<CommonResponseBody<String>>}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponseBody<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // 예외의 실제 원인을 확인
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {

            // 필드 이름 및 잘못된 값 추출
            String fieldName = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("Unknown field");

            String errorMessage = String.format("잘못된 타입입니다. '%s': %s", fieldName, invalidFormatException.getValue());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponseBody<>(errorMessage));
        }

        // 일반적인 JSON 파싱 오류 메시지 처리
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseBody<>("Invalid JSON input"));
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
