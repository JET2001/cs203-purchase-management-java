package com.ticketpurchaseapp.purchase.interceptor;

import java.io.IOException;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ticketpurchaseapp.purchase.util.JwtUtil;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, x-requested-with");
            response.setHeader("Access-Control-Max-Age", "1800");
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
            return false;
        }
        try {
            String userId = JwtUtil.verifyToken(token.substring(7));
            boolean tokenAboutToExpire = JwtUtil.isTokenAboutToExpire(token.substring(7));
            if (tokenAboutToExpire) {
                String newToken = JwtUtil.generateToken(userId);
                response.setHeader("Authorization", "Bearer " + newToken);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
            }
            request.setAttribute("userId", userId);
            return true;
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }
}