package com.abing.provider.service;

import com.abing.brpc.annotation.BRpcService;
import com.abing.example.common.model.User;
import com.abing.example.common.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 13:36
 * @Description
 */
@BRpcService
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {

        System.out.println("brpc 远程调用成功 " + user.getUserName());
        user.setUserName(user.getUserName() + "brpc");
        return user;
    }
}
