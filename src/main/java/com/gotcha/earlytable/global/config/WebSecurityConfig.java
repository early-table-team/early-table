package com.gotcha.earlytable.global.config;

import com.gotcha.earlytable.global.config.filter.JwtAuthFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // SecurityFilterChain 빈 설정을 위해 필요.
public class WebSecurityConfig {

    /**
     * JWT 인증 Filter.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * AuthenticationProvider.
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * AuthenticationEntryPoint.
     */
    private final AuthenticationEntryPoint authEntryPoint;

    /**
     * AccessDeniedHandler.
     */
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * 화이트 리스트.
     */
    private static final String[] WHITE_LIST = { "/users/healthy","/users/register", "/users/login", "/users/refresh", "/error", "/stores/*/waiting/offline", "/stores/*/view/**"};

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter,
                             AuthenticationProvider authenticationProvider,
                             AuthenticationEntryPoint authEntryPoint,
                             AccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 허용할 origin을 설정
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000",
                "https://www.earlytable.kr", "https://earlytable.kr",
                "https://api.earlytable.kr", "https://www.api.earlytable.kr", "https://t1.kakaocdn.net",
                "https://earlytable.store", "https://www.earlytable.store")); // 클라이언트의 주소
        configuration.setAllowedMethods(List.of("*")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더를 허용
        configuration.setAllowCredentials(true); // 자격 증명(쿠키, 인증 헤더 등)을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }

    /**
     * security 필터.
     *
     * @param http {@link HttpSecurity}
     * @return {@link SecurityFilterChain} 필터 체인
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 활성화 및 설정 적용
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(WHITE_LIST).permitAll()
                                // static 리소스 경로
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                // 일부 dispatch 타입
                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE,
                                        DispatcherType.ERROR).permitAll()
                                // path 별로 접근이 가능한 권한 설정
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/owner/**").hasRole("OWNER")
                                // 나머지는 인증이 필요
                                .anyRequest().authenticated()
                )
                // Spring Security 예외에 대한 처리를 핸들러에 위임.
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                // JWT 기반 테스트를 위해 SecurityContext를 가져올 때 HttpSession을 사용하지 않도록 설정.
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                /*
                 * Spring Security와 관련된 예외(AuthenticationException, AccessDeniedException)는
                 * ExceptionTranslationFilter에서 처리가 된다.
                 *
                 * ExceptionTranslationFilter의 doFilter()는 이후의 필터 체인에서 예외가 발생하면 그 예외를 처리하도록 작성되어 있다.
                 * request를 넘겨 JwtAuthFilter에서 발생한 예외를 처리시키기 위해 ExceptionTranslationFilter 다음에 수행하도록 순서를 설정.
                 */
                .addFilterAfter(jwtAuthFilter, ExceptionTranslationFilter.class);

        return http.build();
    }

    /**
     * 사용자 권한의 계층을 설정.
     *
     * @return {@link RoleHierarchy}
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_OWNER\nROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    /**
     * h2-console 접속은 Spring Security를 거치지 않도록 설정.
     *
     * @return {@link WebSecurityCustomizer}
     * @see <a
     * href="https://dukcode.github.io/spring/h2-console-with-spring-security/">spring-security에서-h2-console-사용하기</a>
     */
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring().requestMatchers(PathRequest.toH2Console());
    }
}
