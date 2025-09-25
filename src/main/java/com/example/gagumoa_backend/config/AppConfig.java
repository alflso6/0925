package com.example.gagumoa_backend.config;

import com.example.gagumoa_backend.service.WebScrapingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CommandLineRunner dataInitializer(WebScrapingService webScrapingService) {
        return args -> {
            System.out.println("애플리케이션 시작 시 데이터 초기화 작업을 시작합니다.");
            webScrapingService.scrapeAndSaveProducts();
        };
    }
}