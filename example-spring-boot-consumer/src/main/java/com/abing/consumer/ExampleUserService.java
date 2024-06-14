package com.abing.consumer;

import com.abing.brpc.annotation.BRpcReference;
import com.abing.example.common.model.User;
import com.abing.example.common.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 13:42
 * @Description
 */
@Service
public class ExampleUserService {


    @BRpcReference
    private UserService userService;


    public void test(){
        User user = new User();
        user.setUserName("哈哈哈");
        User rpcUser = userService.getUser(user);
        System.out.println("consumer: " + rpcUser.getUserName());
    }


}
