package com.video.controller;


import com.video.enums.VideoStatusEnum;
import com.video.pojo.Bgm;
import com.video.pojo.Comments;
import com.video.pojo.Videos;
import com.video.service.BgmService;
import com.video.service.VideoService;
import com.video.utils.FileUtils;
import com.video.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/video")
@Api(value = "视频相关业务的接口",tags = {"视频相关业务的controller"})
public class VideoController {

    @Value("${spring.user.video.path}")
    private String ffmpegEXEPath;

    @Value("${spring.user.upload.path}")
    private String uploadPath;

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;

    /**
     * 上传视频并 ffmpeg 处理视频
     * @param file
     * @param userId
     * @param bgmId
     * @param videoSeconds
     * @param videoWidth
     * @param videoHeight
     * @param desc
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传视频",notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "背景音乐id",name = "bgmId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "背景音乐播放长度",name = "videoSeconds",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "视频宽度",name = "videoWidth",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "视频高度",name = "videoHeight",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "视频描述",name = "desc",required = false,dataType = "String",paramType = "form")
    })
    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data")
    public Response uploadVideo(
                    @ApiParam(value = "短视频",required = true) MultipartFile file,
                    String userId,
                    String bgmId,
                    double videoSeconds,
                    int videoWidth,
                    int videoHeight,
                    String desc) throws IOException {
        if (file.isEmpty()){
            return Response.error("请选择图片");
        }
        if (StringUtils.isBlank(userId)){
            return Response.error("参数错误！");
        }
        // 保存到数据库的路径
        String path = "/" + userId + "/video";
        // 图片上传的绝对路径
        String savePath = uploadPath + path;
        String fileName = FileUtils.upload(file, savePath);
        // 保存到数据库的路径
        String dbPath = path + "/" + fileName;
        // 上传完成后，使用 ffmpeg 进行处理
        // ffmpeg 截取视频封面路径
        String coverPath = "/" + userId + "/cover/" + fileName.split("\\.")[0] + ".jpg";
        // 截图视频封面
        FileUtils.converVideoCover(ffmpegEXEPath,savePath + "/" + fileName,uploadPath + coverPath);
        if (StringUtils.isNotBlank(bgmId)){
            Bgm bgm = bgmService.findByBgmId(bgmId);
            String videoInputPath = savePath + "/" + fileName;
            String mp3InputPath = uploadPath + bgm.getPath();
            String videoOutputPath = videoInputPath + "_ffmpeg.mp4";
            dbPath = path + "/" + fileName + "_ffmpeg.mp4";
            FileUtils.converMergreVideoMp3(ffmpegEXEPath,videoInputPath,mp3InputPath,videoSeconds,videoOutputPath);
            // 整合后，删除原来上传的视频
            FileUtils.delete(savePath + "/" + fileName);
        }
        // 保存到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(dbPath);
        video.setCoverPath(coverPath);
        video.setStatus(VideoStatusEnum.SUCCESS.getCode());
        video.setCreateTime(new Date());
        return videoService.save(video);
    }

    /**
     * 分页查询与搜索视频列表（包括个人页面作品列表的查询）
     * @param videos 搜索词 videos.desc
     * @param isSaveRecord 是否保存热搜词 1 - 需要保存,0 - 不需要保存 ，或者为空的时候
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "视频列表",notes = "获取视频列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页数",name = "page",required = false,dataType = "Integer",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数",name = "size",required = false,dataType = "Integer",paramType = "query",defaultValue = "6")
    })
    @PostMapping("/showAll")
    public Response showAll(@RequestBody Videos videos,Integer isSaveRecord, Integer page, Integer size){
        if (page == null){
            page = 1;
        }
        if (size == null){
            size = 6;
        }
        return videoService.findVideosAll(videos,isSaveRecord,page,size);
    }

    /**
     * 查询个人收藏作品列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "视频列表",notes = "获取视频列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "当前页数",name = "page",required = false,dataType = "Integer",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数",name = "size",required = false,dataType = "Integer",paramType = "query",defaultValue = "6")
    })
    @PostMapping("/showMyLike")
    public Response showMyLike(String userId, Integer page, Integer size){
        if (page == null){
            page = 1;
        }
        if (size == null){
            size = 6;
        }
        return videoService.findVideosAllMyLike(userId,page,size);
    }

    /**
     * 查询个人收藏作品列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "视频列表",notes = "获取视频列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "当前页数",name = "page",required = false,dataType = "Integer",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数",name = "size",required = false,dataType = "Integer",paramType = "query",defaultValue = "6")
    })
    @PostMapping("/showMyFollow")
    public Response showMyFollow(String userId, Integer page, Integer size){
        if (page == null){
            page = 1;
        }
        if (size == null){
            size = 6;
        }
        return videoService.findVideosAllMyFollow(userId,page,size);
    }

    /**
     * 获取热搜词
     * @return
     */
    @ApiOperation(value = "获取热搜词",notes = "获取热搜词的接口")
    @GetMapping("/hot")
    public Response<List<String>> hot(){
        return videoService.findByCountContextHotWords();
    }

    /** 注意：这里增加 ffmpeg 截图视频封面后，该方法弃用
     * 上传视频封面并更新到数据库
     * @param file
     * @param userId
     * @param videoId
     * @return
     */
    @ApiOperation(value = "上传封面",notes = "上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "视频id",name = "videoId",required = true,dataType = "String",paramType = "form")
    })
    @PostMapping(value = "/uploadCover",headers = "content-type=multipart/form-data")
    public Response updateCover(@ApiParam(value = "视频封面",required = true) MultipartFile file,String userId,String videoId) throws IOException {
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(videoId)){
            return Response.error("用户id和用户id不能为空");
        }
        // 保存数据库路径
        String path = "/" + userId + "/cover";
        // 图片上传的绝对路径
        String savePath = uploadPath + path;
        String fileName = FileUtils.upload(file,savePath);
        return videoService.updateVideoConver(videoId,path + "/" + fileName);
    }

    /**
     * 添加用户收藏与点赞
     * @param userId 当前用户id (用户视频表增加记录，视频表表用户收藏视频数量字段 +1)
     * @param videoId 当前视频id (用户视频表增加记录，视频表表用户收藏视频数量字段 +1)
     * @param videoCreateUserId 视频发布者id (用户表用户被收藏字段 +1)
     * @return
     */
    @ApiOperation(value = "用户收藏",notes = "用户添加收藏的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "视频id",name = "videoId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "发布者id",name = "videoCreateUserId",required = true,dataType = "String",paramType = "form")
    })
    @GetMapping("/userLike")
    public Response userLike(String userId,String videoId,String videoCreateUserId){
        return videoService.userLikeVideo(userId,videoId,videoCreateUserId);
    }

    /**
     * 取消收藏
     * @param userId
     * @param videoId
     * @param videoCreateUserId
     * @return
     */
    @ApiOperation(value = "取消收藏",notes = "用户取消搜查的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id",name = "userId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "视频id",name = "videoId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "发布者id",name = "videoCreateUserId",required = true,dataType = "String",paramType = "form")
    })
    @GetMapping("/userUnLike")
    public Response userUnLike(String userId,String videoId,String videoCreateUserId){
        return videoService.userUnLikeVideo(userId,videoId,videoCreateUserId);
    }

    /**
     * 保存留言
     * @param comments
     * @return
     */
    @ApiOperation(value = "视频留言",notes = "用户给视频留言的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "评论表父级id",name = "fatherCommentId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "被评论者id",name = "toUserId",required = true,dataType = "String",paramType = "form")
    })
    @PostMapping("/saveComment")
    public Response<String> saveComment(@RequestBody Comments comments,String fatherCommentId,String toUserId){
        if (StringUtils.isNotBlank(fatherCommentId) && StringUtils.isNotBlank(toUserId)){
            comments.setFatherCommentId(fatherCommentId);
            comments.setToUserId(toUserId);
        }
        return videoService.saveComment(comments);
    }

    /**
     * 获取评论分页列表
     * @param videoId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "评论列表",notes = "这是获取评论分页分页列表的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "视频id",name = "videoId",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(value = "当前页码",name = "page",required = false,dataType = "Integer",paramType = "form",defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数",name = "size",required = false,dataType = "Integer",paramType = "form",defaultValue = "6")
    })
    @GetMapping("/getVideoComments")
    public Response getVideoComments(String videoId,Integer page,Integer size){
        if (page == null){
            page = 1;
        }
        if (size == null){
            size = 6;
        }
        return videoService.findCommentsAll(videoId,page,size);
    }
}
