package com.abing.example.provider;

import com.abing.example.common.model.User;
import com.abing.example.common.service.UserService;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:17
 * @Description
 */
public class UserServiceImpl implements UserService {


    public User getUser(User user) {


        System.out.println("用户名: = " + user);


        return user;
    }
}
