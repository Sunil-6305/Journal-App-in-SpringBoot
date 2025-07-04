package com.sunil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 🔥 Add this line
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 🌐 All endpoints
                .allowedOrigins("http://localhost:3000") // ✅ React App
                .allowedMethods("*") // ✅ GET, POST, PUT, DELETE
                .allowedHeaders("*")
                .allowCredentials(true); // ✅ JWT ke liye zaroori
    }
}