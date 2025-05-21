package com.example.security_app.config;

import com.example.security_app.exceptionSecurity.CustomAccessDeniedHandler;
import com.example.security_app.exceptionSecurity.CustomAuthenticationEntryPoint;
import com.example.security_app.filter.JwtAuthenticationFilter;

import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // public endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // user endpoints
                        .requestMatchers("/users/profile").hasRole("USER")  //.hasAnyRole("USER", "ADMIN", "MODERATOR")

                        // moderator endpoints
                        .requestMatchers("/moderator/**").hasRole("MODERATOR") //hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.GET, "/users/*").hasRole("MODERATOR")

                        // admin endpoint
                        .requestMatchers("/admin/**").hasRole("ADMIN")


                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Обработка 401 Unauthorized и 403 Forbidden
                .exceptionHandling(ex -> ex
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )

                // Фильтрация JWT перед UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
