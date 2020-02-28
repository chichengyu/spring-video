package com.video.service;

import com.video.pojo.Comments;
import com.video.pojo.Videos;
import com.video.utils.Response;

import java.util.List;
import java.util.Map;

public interface VideoService {

    /**
     * 保存视频
     * @param videos
     * @return
     */
    Response save(Videos videos);

    /** 分页查询与搜索视频列表（包括个人页面作品列表的查询）,以及搜索时新增热搜词
     * 查询所有视频，链表 users 查
     * @param page
     * @param size
     * @return
     */
    Response<Map> findVideosAll(Videos videos,Integer isSaveRecord,Integer page, Integer size);

    /**
     * 查询个人收藏作品列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    Response<Map> findVideosAllMyLike(String userId, Integer page, Integer size);

    /**
     * 查询我关注的人发的视频
     * @param userId
     * @param page
     * @param size
     * @return
     */
    Response findVideosAllMyFollow(String userId, Integer page, Integer size);

    /**
     * 热搜词统计查询
     * @return
     */
    Response<List<String>> findByCountContextHotWords();

    /** （未用到）
     * 更新视频封面
     * @param videoId
     * @param converPath
     * @return
     */
    Response<String> updateVideoConver(String videoId, String converPath);

    /**
     * 用户收藏与点赞添加
     * @param userId
     * @param videoId
     * @param videoCreateUserId
     * @return
     */
    Response userLikeVideo(String userId, String videoId, String videoCreateUserId);

    /**
     * 取消用户收藏与点赞
     * @param userId
     * @param videoId
     * @param videoCreateUserId
     * @return
     */
    Response userUnLikeVideo(String userId, String videoId, String videoCreateUserId);

    /**
     * 保存留言
     * @param comments
     * @return
     */
    Response<String> saveComment(Comments comments);

    /**
     * 获取评论分页列表
     * @param videoId
     * @param page
     * @param size
     * @return
     */
    Response<Map> findCommentsAll(String videoId, Integer page, Integer size);
}
