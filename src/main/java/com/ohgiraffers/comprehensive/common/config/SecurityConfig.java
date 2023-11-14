package com.ohgiraffers.comprehensive.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {

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
                .and()
                // 교차 출저 자원 공유 설정
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
}
