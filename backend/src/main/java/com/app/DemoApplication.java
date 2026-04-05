package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        // Launch the Spring Boot application
        SpringApplication.run(DemoApplication.class, args);

        logger.info("===============================================");

        logger.info("🚀 IU WEBSITE - INTERNAL SYSTEM IS READY!");

        logger.info("📍 Access URL: http://localhost:8080/api");

        logger.info("👥 Roles: Student & Teacher");

        logger.info("===============================================");
    }
}