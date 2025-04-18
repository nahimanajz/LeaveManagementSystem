package com.leave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.leave")
public class LeaveManagementApplication {
    public static void main(String[] args) {
        System.out.println("Starting Leave Management Application...");
        SpringApplication.run(LeaveManagementApplication.class, args);
    }
} 