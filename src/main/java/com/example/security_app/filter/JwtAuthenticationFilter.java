package com.example.security_app.filter;

import com.example.security_app.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Извлекаем заголовок Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Получаем сам токен (убираем "Bearer ")
        String token = authHeader.substring(7);
        System.out.println("🔍 JWT фильтр сработал. Токен: " + token);

        try {
            // Извлекаю username из токена
            String username = jwtUtil.extractUsername(token);

            // Проверяю, не аутентифицирован ли уже этот пользователь
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Загружаю пользователя из базы
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Проверка действителен ли токен
                if (jwtUtil.validateToken(token, userDetails)){

                    // Создаю объект аутентификации
                    List<String> roles = jwtUtil.extractRoles(token);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    // Устанавливаю аутентификацию в SecurityContext
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.info("JWT token is invalid: " + e.getMessage());
        }

        // Передаем запрос дальше
        filterChain.doFilter(request, response);
    }
}
