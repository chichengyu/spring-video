package com.video.controller;


import com.video.pojo.Users;
import com.video.service.UserService;
import com.video.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 登陆
     * @param username
     * @param password
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Response<Map> login(String username, String password, HttpServletRequest request){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Response.error("账号或密码不能为空！");
        }
        Users users = userService.checkLogin(username);
        if (users == null){
            return Response.error("账号不存在！");
        }
        if (!BCrypt.checkpw(password,users.getPassword())){
            return Response.error("密码错误！");
        }
        request.getSession(true).setAttribute("session_user",users);

        return Response.success("登陆成功！");
    }

    /**
     * 退出登陆
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "login";
    }
}
