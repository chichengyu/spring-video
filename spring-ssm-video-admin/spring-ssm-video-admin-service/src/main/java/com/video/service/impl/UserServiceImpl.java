package com.video.service.impl;

import com.video.dao.UsersDao;
import com.video.pojo.Users;
import com.video.service.UserService;
import com.video.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersDao usersDao;

    /**
     * 登陸
     * @param username
     * @return
     */
    @Override
    public Users checkLogin(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = usersDao.selectOne(users);
        return result == null ? null : result;
    }
}
