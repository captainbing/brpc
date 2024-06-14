package com.abing.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 13:43
 * @Description
 */
@SpringBootTest
class ExampleUserServiceTest {


    @Resource
    private ExampleUserService exampleUserService;

    @Test
    void test1() {
        exampleUserService.test();
    }
}