package com.video.controller;

import com.video.pojo.Bgm;
import com.video.service.BgmService;
import com.video.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bgm")
@Api(value = "背景音乐业务的接口",tags = {"背景音乐业务的controller"})
public class BgmController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "获取背景音乐列表",notes = "获取背景音乐列表的接口")
    @GetMapping("/list")
    public Response<List<Bgm>> list(){
        return bgmService.findBgmList();
    }
}
