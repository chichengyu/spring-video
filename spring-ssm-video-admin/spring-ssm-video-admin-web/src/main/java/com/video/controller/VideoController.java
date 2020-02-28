package com.video.controller;

import com.video.pojo.Bgm;
import com.video.service.BgmService;
import com.video.utils.FileUtils;
import com.video.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/video")
public class VideoController {

    private static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private BgmService bgmService;

    /**
     * bgm列表页面
     * @return
     */
    @GetMapping("/showBgmList")
    public String showBgmList(){

        return "video/bgmList";
    }

    /**
     * 查询 bgm 列表
     * @param page
     * @return
     */
    @PostMapping("/queryBgmList")
    @ResponseBody
    public Map queryBgmList(Integer page){
        return bgmService.queryBgmList(page,3);
    }

    /**
     * 显示添加 bgm 页面
     * @return
     */
    @GetMapping("/showAddBgm")
    public String showAddBgm(){
        return "video/addBgm";
    }

    /**
     * 添加 bgm 到数据库
     * @return
     */
    @PostMapping("/addBgm")
    @ResponseBody
    public Response<String> addBgm(Bgm bgm){
        return bgmService.addBgm(bgm);
    }

    /**
     * 删除音乐
     * @param bgmId
     * @return
     */
    @PostMapping("/delBgm")
    @ResponseBody
    public Response<String> delBgm(String bgmId){
        return bgmService.deleteBgm(bgmId);
    }

    /**
     * 上传音乐
     * @param file
     * @return
     */
    @PostMapping("/bgmUpload")
    @ResponseBody
    public Response bgmUpload(MultipartFile file){
        if (file.isEmpty()){
            return Response.error("请选择音乐！");
        }
        // 保存到卢数据库的路径
        String path = "/bgm";
        // 上传保存路径
        String savePath = "G:/video-upload/bgm";
        String fileName = "";
        try {
            fileName = FileUtils.upload(file, savePath);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("【音乐上传异常】，{}",e);
        }
        Map map = new HashMap();
        map.put("url",path + "/" + fileName);
        return Response.success(map);
    }

    /**
     * 举报列表
     * @return
     */
    @GetMapping("/showReportList")
    public String showReportList(){
        return "video/reportList";
    }
}
