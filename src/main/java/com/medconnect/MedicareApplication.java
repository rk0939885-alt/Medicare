package com.medconnect;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication  
public class MedicareApplication {
    public static void main(String[] args) {
        
        SpringApplication.run(MedicareApplication.class, args);

    }
    @PostConstruct
    public void init() {
        // Setting Spring Boot Default TimeZone to Asia/Kolkata
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }
}