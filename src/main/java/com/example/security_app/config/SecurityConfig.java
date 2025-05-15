package com.example.security_app.config;

import com.example.security_app.filter.JwtAuthenticationFilter;
import com.example.security_app.repository.UserRepository;
import com.example.security_app.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // public endpoints
                        .requestMatchers("/auth/login", "/users/create").permitAll()

                        // user endpoints
                        .requestMatchers("/users/profile").hasRole("USER")  //.hasAnyRole("USER", "ADMIN", "MODERATOR")

                        // moderator endpoints
                        .requestMatchers("/moderator/**").hasRole("MODERATOR") //hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.GET, "/users/*").hasRole("MODERATOR")

                        // admin endpoint
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/all").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Добавляем обработку 401 Unauthorized (если нет токена или он невалиден)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("🔐 401 Unauthorized — токен отсутствует или недействителен");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("🚫 403 Forbidden — недостаточно прав");
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        })
                )

                // Подключаем JWT фильтр перед UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
