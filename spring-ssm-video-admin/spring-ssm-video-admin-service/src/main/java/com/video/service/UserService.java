package com.video.service;


import com.video.pojo.Users;

public interface UserService {

    /**
     * 登陸
     * @param username
     * @return
     */
    Users checkLogin(String username);
}
