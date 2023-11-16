package com.ticketpurchaseapp.purchase.configure;

import com.ticketpurchaseapp.purchase.interceptor.JwtInterceptor;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigure implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Autowired
    WebConfigure(JwtInterceptor jwtInterceptor){
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/users/**")
                .addPathPatterns("/events-register/**")
                .addPathPatterns("/purchase/seat-category-selection")
                .excludePathPatterns("/purchase/**")
                .excludePathPatterns("/auth/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");

    }
}