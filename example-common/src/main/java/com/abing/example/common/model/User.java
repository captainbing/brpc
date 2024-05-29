package com.abing.example.common.model;

import java.io.Serializable;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:12
 * @Description
 */
public class User implements Serializable {


    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
