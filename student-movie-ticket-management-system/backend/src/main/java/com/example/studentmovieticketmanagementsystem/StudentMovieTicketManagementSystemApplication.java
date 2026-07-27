package com.example.studentmovieticketmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.studentmovieticketmanagementsystem",
        "config",
        "handler",
        "repository",
        "service"
})
public class StudentMovieTicketManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentMovieTicketManagementSystemApplication.class, args);
    }

}
