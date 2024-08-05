package com.synclab.triphippie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Consentire da tutte le origini
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Consentire tutti i metodi HTTP
                .allowedHeaders("*"); // Consentire tutti gli header HTTP
    }
}
