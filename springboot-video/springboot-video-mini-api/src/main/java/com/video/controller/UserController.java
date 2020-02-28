package com.video.controller;

import com.video.pojo.Users;
import com.video.pojo.UsersReport;
import com.video.pojo.vo.PublisherVideoVo;
import com.video.pojo.vo.UsersVo;
import com.video.service.UserService;
import com.video.utils.FileUtils;
import com.video.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Api(value = "用户相关业务的接口",tags = {"用户相关业务的controller"})
public class UserController {

    @Value("${spring.user.upload.path}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    /**
     * 用户上传头像
     * @param file
     * @param userId
     * @return
     */
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "form")
    @ApiOperation(value = "用户上传头像",notes = "用户上传头像的接口")
    @PostMapping(value = "/uploadFace",headers = "content-type=multipart/form-data")
    public Response uploadFace(@ApiParam(value = "上传头像",required = true) MultipartFile file, String userId) throws IOException {
        if (file.isEmpty()){
            return Response.error("请选择图片");
        }
        if (StringUtils.isBlank(userId)){
            return Response.error("参数错误！");
        }
        // 保存到数据库的路径
        String path = "/" + userId + "/face";
        // 图片上传的绝对路径
        String savePath = uploadPath + path;
        String fileName = FileUtils.upload(file, savePath);
        // 先删除原来的图片
        Users byIdUser = userService.findByIdUser(userId);
        if (byIdUser != null && StringUtils.isNotBlank(byIdUser.getFaceImage())){
            FileUtils.delete(uploadPath + byIdUser.getFaceImage());
        }
        // 更新用户信息
        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(path + "/" + fileName);
        boolean result = userService.updateUserInfo(users);
        if (result){
            Map map = new HashMap();
            map.put("url",path + "/" + fileName);
            return Response.success(map);
        }
        return Response.error("更新信息失败！");
    }

    /**
     * 查询用户信息
     * @param userId
     * @param fanId
     * @return
     */
    @ApiOperation(value = "查询用户信息",notes = "查询用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "粉丝id",name = "fanId",required = true,dataType = "String",paramType = "query")
    })
    @GetMapping("/query")
    public Response queryUserInfo(String userId,String fanId){
        return userService.findByUserIdForUser(userId,fanId);
    }

    /**
     * 视频详情页进入请求，查询当前登陆用户与视频的关系，是否收藏
     * @param loginUserId
     * @param videoId
     * @param publishUserId
     * @return
     */
    @ApiOperation(value = "sds")
//    @ApiImplicitParams({
//            @ApiImplicitParam()
//    })
    @GetMapping("/queryPublisher")
    public Response<PublisherVideoVo> queryPublisher(String loginUserId,String videoId,String publishUserId){
        if (StringUtils.isBlank(publishUserId)){
            return Response.error("参数错误");
        }
        // 1. 查询视频发布者的信息
        Users users = userService.findByIdUser(publishUserId);
        UsersVo publisherUsersVo = new UsersVo();
        BeanUtils.copyProperties(users,publisherUsersVo);
        // 2. 查询当前登录者和视频的点赞关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);

        // 返回数据
        PublisherVideoVo publisherVideoVo = new PublisherVideoVo();
        publisherVideoVo.setPublisher(publisherUsersVo);
        publisherVideoVo.setUserLikeVideo(userLikeVideo);
        return Response.success(publisherVideoVo);
    }

    /**
     * 添加关注
     * @param userId
     * @param fanId
     * @return
     */
    @ApiOperation(value = "用户关注",notes = "用户添加关注的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "发布者id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "粉丝id",name = "fanId",required = true,dataType = "String",paramType = "form"),
    })
    @GetMapping("/beyourfans")
    public Response beyourfans(String userId,String fanId){
        return userService.saveUserFanRelation(userId,fanId);
    }

    /**
     * 取消关注
     * @param userId
     * @param fanId
     * @return
     */
    @ApiOperation(value = "用户关注",notes = "用户添加关注的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "发布者id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "粉丝id",name = "fanId",required = true,dataType = "String",paramType = "form"),
    })
    @GetMapping("/dontbeyourfans")
    public Response dontbeyourfans(String userId,String fanId){
        return userService.deleteUserFanRelation(userId,fanId);
    }

    /**
     * 用户举报
     * @param usersReport
     * @return
     */
    @ApiOperation(value = "用户举报",notes = "用户举报的接口")
    @PostMapping("/reportUser")
    public Response<String> reportUser(@RequestBody UsersReport usersReport){
        return userService.saveReportUser(usersReport);
    }
}
