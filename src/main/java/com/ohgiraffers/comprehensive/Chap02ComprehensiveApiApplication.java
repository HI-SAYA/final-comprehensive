package com.ohgiraffers.comprehensive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Chap02ComprehensiveApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chap02ComprehensiveApiApplication.class, args);
    }

}
