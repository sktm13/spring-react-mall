package org.yujin.mallapi.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.yujin.mallapi.security.CustomUserDetailsService;
import org.yujin.mallapi.security.filter.JWTCheckFilter;
import org.yujin.mallapi.security.handler.APILoginFailHandler;
import org.yujin.mallapi.security.handler.APILoginSuccessHandler;
import org.yujin.mallapi.security.handler.CustomAccessDeniedHandler;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.csrf(csrf -> csrf.disable());

        http.httpBasic(httpBasic -> httpBasic.disable());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.userDetailsService(customUserDetailsService);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/health").permitAll()
                .requestMatchers("/api/member/login").permitAll()
                .requestMatchers("/api/member/kakao").permitAll()
                .requestMatchers("/api/member/refresh").permitAll()
                .requestMatchers("/api/products/view/**").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginProcessingUrl("/api/member/login")
                .successHandler(new APILoginSuccessHandler())
                .failureHandler(new APILoginFailHandler())
        );

        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(apiAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                org.springframework.security.core.AuthenticationException authException) -> {

            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "ERROR_UNAUTHORIZED");
        };
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            int status,
            String errorMessage) throws IOException {

        Gson gson = new Gson();

        String json = gson.toJson(Map.of("error", errorMessage));

        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.close();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}