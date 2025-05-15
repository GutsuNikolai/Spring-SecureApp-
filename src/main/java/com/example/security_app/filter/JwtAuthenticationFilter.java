package com.example.security_app.filter;

import com.example.security_app.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

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
            // Извлекаем username из токена
            String username = jwtUtil.extractUsername(token);

            // Проверяем, не аутентифицирован ли уже этот пользователь
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Загружаем пользователя из базы
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // TODO возможно не проверяется на пренадлежность пользвоателю
                // Проверяем, действителен ли токен
                if (jwtUtil.validateToken(token, userDetails)){

                    // Создаем объект аутентификации
                    List<String> roles = jwtUtil.extractRoles(token);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    // Устанавливаем аутентификацию в SecurityContext
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            System.out.println("JWT token is invalid: " + e.getMessage());
        }

        // Передаем запрос дальше
        filterChain.doFilter(request, response);
    }
}
