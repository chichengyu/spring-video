package com.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    /**
     * 后台默认首页
     * @return
     */
    @GetMapping("/center")
    public String index(){
        return "center";
    }
}
