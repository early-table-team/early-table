package com.gotcha.earlytable.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  /**
   * UserDetailsService.
   */
  private final UserDetailsService userDetailsService;

  /**
   * PasswordEncoder(암호 처리기).
   *
   * @return {@link BCryptPasswordEncoder}
   */
  @Bean
  BCryptPasswordEncoder passwordEncoderForSecurity() {
    return new BCryptPasswordEncoder();
  }

  /**
   * AuthenticationManager(인증 관리자).
   *
   * @param config {@link AuthenticationConfiguration}
   * @return 설정이 추가된 AuthenticationManager
   */
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    // "AuthenticationManager 에 위임."
    return config.getAuthenticationManager();
  }

  /**
   * AuthenticationProvider(인증 공급자).
   *
   * @return {@link AuthenticationProvider}
   */
  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(this.userDetailsService);

    authProvider.setPasswordEncoder(passwordEncoderForSecurity());

    return authProvider;
  }
}
