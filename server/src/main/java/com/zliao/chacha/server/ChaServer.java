package com.zliao.chacha.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zliao.chacha")
public class ChaServer {
    public static void main(String[] args) {
        SpringApplication.run(ChaServer.class, args);
    }
}
