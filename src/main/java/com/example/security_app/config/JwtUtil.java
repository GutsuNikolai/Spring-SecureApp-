package com.example.security_app.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // Секретный ключ, который мы будем использовать для подписи токенов
    private static final String SECRET_KEY = "superSecretKeyForJWTsuperSecretKeyForJWT";  // Секретный ключ для подписи токена
    private static final long EXPIRATION_TIME = 86400000;  // Время жизни токена (1 день, в миллисекундах)

    // Создаем ключ для подписи с помощью библиотеки jjwt
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Метод для генерации JWT токена
    public static String generateToken(String username, List<String> roles) {
        return Jwts.builder()  // Начинаем строить токен
                .setSubject(username)  // Добавляем имя пользователя в токен
                .claim("roles", roles)   // Добавляем роль пользователя в токен
                .setIssuedAt(new Date())  // Устанавливаем время создания токена
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Устанавливаем время истечения токена
                .signWith(key, SignatureAlgorithm.HS256)  // Подписываем токен с помощью нашего секретного ключа
                .compact();  // Генерируем строку токена
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Указываем ключ для проверки подписи токена
                .build()
                .parseClaimsJws(token)  // Парсим токен, получаем его содержимое
                .getBody();  // Извлекаем тело токена
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // Метод для проверки токена
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }
}
