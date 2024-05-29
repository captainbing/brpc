package com.abing.example.common.service;

import com.abing.example.common.model.User;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:13
 * @Description
 */
public interface UserService {


    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);

    default int getNumber(){
        return 10;
    }

}
