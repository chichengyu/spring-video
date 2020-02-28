package com.video.controller;

import com.video.pojo.Users;
import com.video.service.UserService;
import com.video.utils.Response;
import com.video.pojo.vo.UsersVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="用户注册登录的接口",tags = {"注册和登录的controller"})
public class RegistLoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     * @param users
     * @return
     */
    @ApiOperation(value = "用户登陆",notes = "用户登陆的接口")
    @PostMapping("/login")
    public Response<UsersVo> login(@RequestBody Users users){
        return userService.findByUsernameAndPasswordForLogin(users.getUsername(), users.getPassword());
    }

    /**
     * 用户注销
     * @param userId
     * @return
     */
    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @GetMapping("/logout")
    public Response logout(String userId){
        return userService.logout(userId);
    }

    /**
     * 用户注册
     * @param users
     * @return
     */
    @ApiOperation(value="用户注册", notes="用户注册的接口")
    @PostMapping("/regist")
    public Response<UsersVo> register(@RequestBody Users users){
        return userService.register(users);
    }
}
