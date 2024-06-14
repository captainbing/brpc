package com.abing.consumer;

import com.abing.brpc.annotation.EnableBRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBRpc
public class ExampleSpringBootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringBootConsumerApplication.class, args);
    }

}
