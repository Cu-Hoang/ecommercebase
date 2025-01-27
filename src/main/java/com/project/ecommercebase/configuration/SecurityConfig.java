package com.project.ecommercebase.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.project.ecommercebase.exception.CustomAccessDeniedHandler;
import com.project.ecommercebase.exception.JwtCustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] PUBLIC_ENDPOINTS = {
        "/users/register",
        "/users/register/verify-email-code",
        "/users/register/customer",
        "/users/register/vendor",
        "/auths/email-otp-password",
        "/auths/login-otp",
        "/auths/email-otp-reset-password",
        "/auths/reset-password",
        "/auths/login-password",
        "/auths/generate-new-token"
    };

    @Value("${spring.jwt.key}")
    private String key;

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                        .permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(
                Arrays.asList("Authorization", "X-Refresh-Token", "Origin", "Content-Type", "Accept"));
        corsConfiguration.setAllowCredentials(Boolean.valueOf(false));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter JwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return JwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtCustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
