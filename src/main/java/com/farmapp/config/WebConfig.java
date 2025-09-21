package com.farmapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // tất cả API
                .allowedOrigins(
                        "http://localhost:5173",
                        "https://farm-app-fe.vercel.app/"
                ) // FE localhost + FE vercel
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // phải có OPTIONS
                .allowedHeaders("*")
                .allowCredentials(false); // chưa dùng cookie/session thì false luôn cho gọn
    }
}

