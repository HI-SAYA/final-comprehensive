package com.ohgiraffers.comprehensive.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.comprehensive.jwt.service.JwtService;
import com.ohgiraffers.comprehensive.login.filter.CustomUsernamePasswordAuthenticationFilter;
import com.ohgiraffers.comprehensive.login.handler.LoginFailureHandler;
import com.ohgiraffers.comprehensive.login.handler.LoginSuccessHandler;
import com.ohgiraffers.comprehensive.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final LoginService loginService;
    private final JwtService jwtService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // csrf 설정 비활성화
                .csrf()
                .disable()
                // API 서버는 session을 사용하지 않으므로 STATELESS 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 요청에 대한 권한 체크
                .authorizeRequests()
                // 클라이언트가 외부 도메인을 요청하는 경우 웹 브라우저에서 자체적으로 사전 요청(preflight)이 일어남
                // 이 때 OPTIONS 메서드로 서버에 사전 요청을 보내 권한을 확인함
                // method / url / pattern
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/api/v1/product-management/**", "/api/vi/products/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                // 로그인 필터 설정
                .addFilterBefore(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 기존에 있던 필터 앞에 custom 필터를 끼워 넣겠다.
                // 교차 출처 자원 공유 설정
                .cors()
                .and()
                .build();
    }

    /* CORS(Cross Origin Resource Sharing) : 교차 출처 자원 공유
    * 보안상 웹 브라우저는 다른 도메인에서 서버의 자원을 요청하는 경우 막아 놓았음.
    * 기본적으로 서버에서 클라이언트를 대상으로 리소스 허용 여부를 결정함. */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 로컬 React에서 오는 요청은 허용한다.
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // ip와 port(http 포함)(Orings)을 허용한다.
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                "Content-Type", "Authorization", "X-Requested-With"));
        // 모든 요청 url 패턴에 대해 위의 설정을 적용한다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /* 비밀번호 암호화 */
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); } // random salting

    /* 인증 매니저 빈 등록 =>
    로그인 시 사용할 password encode 설정,
    로그인 시 유저 조회하는 메소드를 가진 Service 클래스 설정

    * ** 로그인 필터가 사용할 인증 매니저 생성*/
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // 체크
        provider.setUserDetailsService(loginService);               // 조회
        return new ProviderManager(provider);
    }

    /* 로그인 실패 핸들러 빈 등록 */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    /* 로그인 성공 핸들러 빈 등록 */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() { return new LoginSuccessHandler(jwtService); }

    /* 로그인 필터 빈 등록 */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        /* 사용할 인증 매니저 설정 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        /* 로그인 실패 핸들링 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        /* 로그인 성공 핸들링 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());

        return customUsernamePasswordAuthenticationFilter;
    }


}
