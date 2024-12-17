package com.example.user_service.domain.dto;

import com.example.user_service.domain.RequestContextUserId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestContextFilterMy extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Long userId = extractUserIdFromRequest(request);
        RequestContextUserId.setUserId(userId);
        log.info("User ID set in RequestContextUserId: {}", userId);

        filterChain.doFilter(request, response);
    }
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return Long.parseLong(token);
    }
}
