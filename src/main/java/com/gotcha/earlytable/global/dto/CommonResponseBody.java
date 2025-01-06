package com.gotcha.earlytable.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;


@Getter
public class CommonResponseBody<T> {

  /**
   * Response 메세지.
   */
  private final String message;

  /**
   * Response 데이터.
   */
  @JsonInclude(Include.NON_NULL)
  private final T data;

  /**
   * 생성자.
   *
   * @param message Response 메세지
   * @param data    Response 데이터
   */
  public CommonResponseBody(String message, T data) {
    this.message = message;
    this.data = data;
  }

  /**
   * 생성자. {@code data} 필드는 {@code null}로 초기화 합니다.
   *
   * @param message Response 메세지
   */
  public CommonResponseBody(String message) {
    this(message, null);
  }
}
