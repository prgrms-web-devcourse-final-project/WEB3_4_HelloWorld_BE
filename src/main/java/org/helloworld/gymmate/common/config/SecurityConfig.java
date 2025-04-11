package org.helloworld.gymmate.common.config;

import java.util.Arrays;
import java.util.List;

import org.helloworld.gymmate.security.filter.CustomAuthenticationFilter;
import org.helloworld.gymmate.security.handler.LoginSuccessHandler;
import org.helloworld.gymmate.security.handler.LogoutSuccessHandler;
import org.helloworld.gymmate.security.resolver.CustomAuthorizationRequestResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;

    @Value("${client.url}")
    private String clientUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .anyRequest() // 나머지
                    .authenticated())
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .oauth2Login(oauth2Login ->
                oauth2Login.successHandler(loginSuccessHandler)
                    .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestResolver(
                        customAuthorizationRequestResolver)))
            .logout(logout -> logout
                .addLogoutHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .logoutSuccessUrl(clientUrl)
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                })
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(Environment env) {
        return web -> {
            // 공통 무시 경로 설정
            web.ignoring()
                .requestMatchers(
                    "/", "/actuator/**", "/v3/**", "/swagger-ui/**",
                    "/api/logistics", "/error"
                )
                .requestMatchers(HttpMethod.GET, "/ptProduct", "/ptProduct/{id:\\d+}", "/ptProduct/trainer/{id:\\d+}")
                .requestMatchers(HttpMethod.GET, "/trainer/list")
                .requestMatchers(HttpMethod.GET, "/gym/{id:\\d+}/facility")
                .requestMatchers(HttpMethod.GET, "/gym/{id:\\d+}/trainer")
                .requestMatchers(HttpMethod.GET, "/gym/{id:\\d+}")
                .requestMatchers(HttpMethod.GET, "/gym")
                .requestMatchers(HttpMethod.GET, "/dummy/**")
                .requestMatchers(HttpMethod.POST, "/dummy/**");

            // 개발 환경에서 추가로 무시할 경로
            if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
                web.ignoring()
                    .requestMatchers("/crawl/**")
                    .requestMatchers(PathRequest.toH2Console());
            }

            // 단위 테스트 환경에서 추가로 무시할 경로
            if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
                web.ignoring()
                    .requestMatchers("/h2-console/**", "/crawl/**")
                    .requestMatchers(PathRequest.toH2Console());
            }
        };
    }

    @Bean
    public FilterRegistrationBean<CustomAuthenticationFilter> registration(CustomAuthenticationFilter filter) {
        FilterRegistrationBean<CustomAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            List.of("http://localhost:3000", "https://web-3-4-hello-world-fe.vercel.app",
                "https://api.gymmate.my")); // 허용할 Origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH")); // 허용할 HTTP 메소드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 인증 정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 요청에 대해 적용

        return source;
    }
}