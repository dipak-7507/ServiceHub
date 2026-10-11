package com.servicehub.config;

import com.servicehub.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        // public
                        .requestMatchers(
                                "/customers/login",
                                "/providers/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/error"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/customers",
                                "/providers"
                        ).permitAll()

                        // any logged-in user can browse providers
                        .requestMatchers(HttpMethod.GET,
                                "/providers",
                                "/providers/category/*"
                        ).authenticated()

                        // role based
                        .requestMatchers("/customers/**").hasRole("CUSTOMER")
                        .requestMatchers("/providers/**").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.POST,
                                "/bookings",
                                "/feedbacks",
                                "/requests"
                        ).hasRole("CUSTOMER")
                        .requestMatchers("/dashboard/**").hasRole("PROVIDER")

                        // everything else: just be logged in
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}