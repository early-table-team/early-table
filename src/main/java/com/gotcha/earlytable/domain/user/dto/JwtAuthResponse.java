package com.gotcha.earlytable.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtAuthResponse {

  /**
   * access token 인증 방식.
   */
  private String tokenAuthScheme;

  /**
   * access token.
   */
  private String accessToken;

  /**
   * 생성자.
   */
  public JwtAuthResponse(String tokenAuthScheme, String accessToken) {
    this.tokenAuthScheme = tokenAuthScheme;
    this.accessToken = accessToken;
  }
}
