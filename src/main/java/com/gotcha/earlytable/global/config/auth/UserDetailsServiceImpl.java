package com.gotcha.earlytable.global.config.auth;

import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "Security::UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

  /**
   * User entity의 repository.
   */
  private final UserRepository userRepository;

  /**
   * 생성자 의존성 주입
   */
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

    /**
   * 입력받은 이메일에 해당하는 사용자 정보를 찾아 리턴.
   *
   * @param username username
   * @return 해당하는 사용자의 {@link UserDetailsImpl} 객체
   * @apiNote 이 애플리케이션에서는 사용자의 이메일을 {@code username}으로 사용합니다
   */
  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = this.userRepository.findByEmailOrElseThrow(username);

    return new UserDetailsImpl(user);
  }
}
