package com.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.video.dao.CommentsDao;
import com.video.dao.SearchRecordsDao;
import com.video.dao.UsersDao;
import com.video.dao.UsersLikeVideosDao;
import com.video.dao.VideosDao;
import com.video.pojo.Comments;
import com.video.pojo.vo.CommentsVo;
import com.video.pojo.vo.VideosVo;
import com.video.pojo.SearchRecords;
import com.video.pojo.UsersLikeVideos;
import com.video.pojo.Videos;
import com.video.service.VideoService;
import com.video.utils.IdWorker;
import com.video.utils.Response;
import com.video.utils.TimeAgoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosDao videosDao;
    @Autowired
    private SearchRecordsDao searchRecordsDao;
    @Autowired
    private UsersLikeVideosDao usersLikeVideosDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private CommentsDao commentsDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存视频
     * @param videos
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response save(Videos videos) {
        String id = String.valueOf(idWorker.nextId());
        videos.setId(id);
        int resultCount = videosDao.insertSelective(videos);
        if (resultCount > 0){
            Map map = new HashMap<>();
            map.put("videoId",id);
            return Response.success(map);
        }
        return Response.error();
    }

    /** 分页查询与搜索视频列表（包括个人页面作品列表的查询）,以及搜索时新增热搜词
     * 查询所有视频，链表 users 查
     * @param page
     * @param size
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<Map> findVideosAll(Videos videos,Integer isSaveRecord,Integer page, Integer size) {
        // 保存热搜词
        if (isSaveRecord != null && isSaveRecord == 1){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(String.valueOf(idWorker.nextId()));
            searchRecords.setContent(videos.getVideoDesc());
            searchRecordsDao.insertSelective(searchRecords);
        }
        PageHelper.startPage(page,size);
        List<VideosVo> videoAndUsersAll = videosDao.findVideoAndUsersAll(videos.getVideoDesc(),videos.getUserId());
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videoAndUsersAll);
        Map map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("size",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        map.put("pageTotal",pageInfo.getPages());
        return Response.success(map);
    }

    /**
     * 查询个人收藏作品列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Response<Map> findVideosAllMyLike(String userId, Integer page, Integer size) {
        if (StringUtils.isBlank(userId)){
            return Response.error("参数错误");
        }
        PageHelper.startPage(page,size);
        List<VideosVo> videoAndMyLikeAll = videosDao.findVideoAndMyLikeAll(userId);
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videoAndMyLikeAll);
        Map map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("size",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        map.put("pageTotal",pageInfo.getPages());
        return Response.success(map);
    }

    /**
     * 查询我关注的人发的视频
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Response findVideosAllMyFollow(String userId, Integer page, Integer size) {
        if (StringUtils.isBlank(userId)){
            return Response.error("参数错误");
        }
        PageHelper.startPage(page,size);
        List<VideosVo> videoAndMyFollowAll = videosDao.findVideoAndMyFollowAll(userId);
        PageInfo<VideosVo> pageInfo = new PageInfo<>(videoAndMyFollowAll);
        Map map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("size",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        map.put("pageTotal",pageInfo.getPages());
        return Response.success(map);
    }

    /**
     * 热搜词统计查询
     * @return
     */
    @Override
    public Response<List<String>> findByCountContextHotWords() {
        return Response.success(searchRecordsDao.findHotWords());
    }

    /**
     * 更新视频封面
     * @param videoId
     * @param converPath
     * @return
     */
    @Override
    public Response<String> updateVideoConver(String videoId, String converPath) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(converPath);
        int reslutCount = videosDao.updateByPrimaryKeySelective(videos);
        if (reslutCount > 0){
            return Response.success();
        }
        return Response.error();
    }

    /**
     * 添加用户收藏与点赞 +1
     * @param userId
     * @param videoId
     * @param videoCreateUserId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response userLikeVideo(String userId, String videoId, String videoCreateUserId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId) || StringUtils.isBlank(videoCreateUserId)){
            return Response.error("参数错误");
        }
        // 1.用户视频表添加纪录
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        usersLikeVideos.setId(String.valueOf(idWorker.nextId()));
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosDao.insert(usersLikeVideos);
        // 2.视频表收藏数量 +1
        videosDao.addVideoLikeCounts(videoId);
        // 3.用户表视频发布者被收藏数量 +1
        usersDao.addReceiveLikeCounts(videoCreateUserId);
        return Response.success();
    }

    /**
     * 取消用户收藏与点赞 -1
     * @param userId
     * @param videoId
     * @param videoCreateUserId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response userUnLikeVideo(String userId, String videoId, String videoCreateUserId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId) || StringUtils.isBlank(videoCreateUserId)){
            return Response.error("参数错误");
        }
        // 1.删除用户视频表纪录
        Example example = new Example(UsersLikeVideos.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andEqualTo("videoId",videoId);
        usersLikeVideosDao.deleteByExample(example);
        // 2.视频表收藏数量 -1
        videosDao.reduceVideoLikeCounts(videoId);
        // 3.用户表视频发布者被收藏数量 -1
        usersDao.reduceReceiveLikeCounts(videoCreateUserId);
        return Response.success();
    }

    /**
     * 保存留言
     * @param comments
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<String> saveComment(Comments comments) {
        comments.setId(String.valueOf(idWorker.nextId()));
        comments.setCreateTime(new Date());
        if (commentsDao.insertSelective(comments) > 0){
            return Response.success();
        }
        return Response.error();
    }

    /**
     * 获取评论分页列表
     * @param videoId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Response<Map> findCommentsAll(String videoId, Integer page, Integer size) {
        if (StringUtils.isBlank(videoId)){
            return Response.error("参数错误");
        }
        PageHelper.startPage(page,size);
        List<CommentsVo> commentsAll = commentsDao.findCommentsAll(videoId);
        // 吧时间格式化成多少 秒 / 分 / 时 /天前 ，如：1天前
        commentsAll.forEach(el -> el.setTimeAgoStr(TimeAgoUtils.format(el.getCreateTime())));
        PageInfo<CommentsVo> pageInfo = new PageInfo<>(commentsAll);
        Map map = new HashMap<>();
        map.put("page",pageInfo.getPageNum());
        map.put("size",pageInfo.getPageSize());
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        map.put("pageTotal",pageInfo.getPages());
        return Response.success(map);
    }
}
