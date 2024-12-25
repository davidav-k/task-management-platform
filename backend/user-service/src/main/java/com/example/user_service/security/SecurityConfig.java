package com.example.user_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.example.user_service.constant.Constants.STRENGTH;


@Configuration
public class SecurityConfig {

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",       // OpenAPI documentation
            "/swagger-ui/**",        // Swagger UI
            "/swagger-ui.html",      // Swagger UI HTML
    };

    @Value("${api.endpoint.base-url}")
    private String baseUrl;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, baseUrl + "/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, baseUrl + "/user/verify/account").permitAll()
                        .requestMatchers(HttpMethod.POST, baseUrl + "/user/register").permitAll()
//                        .requestMatchers(HttpMethod.GET, baseUrl + "/user/**").access(userRequestAuthorizationManager)
//                        .requestMatchers(HttpMethod.POST, baseUrl + "/user").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.PUT, baseUrl + "/user/**").access(userRequestAuthorizationManager)
//                        .requestMatchers(HttpMethod.DELETE, baseUrl + "/user/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.PATCH, baseUrl + "/user/**").access(userRequestAuthorizationManager)
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))// for H-2 browser console access
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
//                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint))
//                .oauth2ResourceServer(auth2ResourceServer -> auth2ResourceServer
//                        .jwt(Customizer.withDefaults())
//                        .authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
//                        .accessDeniedHandler(customBearerTokenAccessDeniedHandler))
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
//                        SessionCreationPolicy.STATELESS))
                .build();
    }

}
