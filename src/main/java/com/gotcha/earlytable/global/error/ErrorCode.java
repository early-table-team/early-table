package com.gotcha.earlytable.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    NO_SEAT(HttpStatus.BAD_REQUEST,"해당 인원에 맞는 자리가 남아있지 않습니다."),
    REJECT_CANCEL(HttpStatus.BAD_REQUEST,"예약을 취소할 수 없습니다."),
    UNAVAILABLE_RESERVATION_TYPE(HttpStatus.BAD_REQUEST, "예약이 불가능한 가게입니다."),
    UNAVAILABLE_ONSITE_WAITING_TYPE(HttpStatus.BAD_REQUEST, "현장 웨이팅이 불가능한 가게입니다."),
    UNAVAILABLE_REMOTE_WAITING_TYPE(HttpStatus.BAD_REQUEST, "원격 웨이팅이 불가능한 가게입니다."),
    STORE_HOLIDAY(HttpStatus.BAD_REQUEST,"가게 휴무일 입니다."),
    RESERVATION_TIME_ERROR(HttpStatus.BAD_REQUEST,"예약가능한 시간이 아닙니다."),
    WAITING_ERROR(HttpStatus.BAD_REQUEST,"현재 웨이팅 가능 상태가 아닙니다."),
    NOT_FOUND_MENU(HttpStatus.BAD_REQUEST,"가게 내에서 메뉴를 찾을 수 없습니다."),
    NO_MORE_REQUEST_AVAILABLE(HttpStatus.BAD_REQUEST, "더이상 친구 요청이 불가능한 사용자입니다. 관리자에게 문의하세요."),
    FULL_PARTY_PEOPLE(HttpStatus.BAD_REQUEST,"파티가 꽉 찼습니다."),
    NO_INPUT_PHONE(HttpStatus.BAD_REQUEST, "연락처를 입력해주세요."),
    NOT_FOUND_DAY(HttpStatus.BAD_REQUEST,"요청한 요일에 예약이 불가능합니다."),
    ALREADY_REPRESENTATIVE_MENU(HttpStatus.BAD_REQUEST,"이미 대표메뉴입니다."),



    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 틀렸습니다."),
    NO_STORE_OWNER(HttpStatus.UNAUTHORIZED, "가게주인이 아닙니다."),


    // 403 Forbidden
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN,"접근이 거부됐습니다."),
    FORBIDDEN_PERMISSION(HttpStatus.FORBIDDEN, "사용자 권한이 없습니다."),
    FORBIDDEN_LOGIN(HttpStatus.FORBIDDEN,"이미 탈퇴한 유저입니다."),
    FORBIDDEN_PARTY_LEADER(HttpStatus.FORBIDDEN,"파티장이 아닙니다."),
    FORBIDDEN_ACCESS_PTAH(HttpStatus.FORBIDDEN,"잘못된 접근입니다."),
    FORBIDDEN_FRIEND_REQUEST(HttpStatus.FORBIDDEN, "본인의 요청이 아닙니다."),
    FORBIDDEN_PARTY_LEADER_LEAVE(HttpStatus.FORBIDDEN,"파티장은 탈퇴가 안됩니다."),
    FORBIDDEN_PARTY_PEOPLE(HttpStatus.FORBIDDEN,"파티일원이 아닙니다"),
    FORBIDDEN_RESERVATION_END(HttpStatus.FORBIDDEN, "시간 및 취소여부를 확인해주세요."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,"리소스를 찾을 수 없습니다."),
    NOT_MATCHED(HttpStatus.NOT_FOUND, "초대한 사용자와 이메일이 일치하지 않습니다."),
    NOT_FOUND_PARTY(HttpStatus.NOT_FOUND,"파티를 찾을 수 없습니다."),
    NOT_FOUND_ALLERGY_STUFF(HttpStatus.NOT_FOUND, "등록되지 않은 알러지 원재료입니다."),


    // 409 CONFLICT
    DUPLICATE_VALUE(HttpStatus.CONFLICT, "중복된 정보입니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 탈퇴한 사용자 아이디입니다."),
    ALREADY_IN_FRIEND(HttpStatus.CONFLICT,"이미 친구로 등록된 사용자입니다."),
    ALREADY_REQUESTED(HttpStatus.CONFLICT, "이미 수락 대기 중인 요청 건이 존재합니다."),
    ALREADY_IN_ALLERGY_CATEGORY(HttpStatus.CONFLICT, "이미 등록된 카테고리 입니다."),
    ALREADY_IN_ALLERGY_STUFF(HttpStatus.CONFLICT, "이미 등록된 원재료입니다."),
    ALREADY_REGISTERED_ALLERGY_STUFF_IN_MENU(HttpStatus.CONFLICT, "이미 메뉴에 등록된 알러지 원재료입니다."),


    // 423 LOCKED
    LOCK_TIMEOUT(HttpStatus.LOCKED, "락을 획득하는데 시간이 초과되었습니다.")


    ;

    private final HttpStatus httpStatus;
    private final String detail;
}


