package com.abing.provider;

import com.abing.brpc.annotation.EnableBRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBRpc
public class ExampleSpringBoorProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringBoorProviderApplication.class, args);
    }

}
