package com.video.dao;

import com.video.pojo.vo.VideosVo;
import com.video.pojo.Videos;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface VideosDao extends Mapper<Videos>, MySqlMapper<Videos> {

    // 链接 user 表查询
    List<VideosVo> findVideoAndUsersAll(@Param("videoDesc")String videoDesc,@Param("userId")String userId);

    // 查询个人收藏作品列表
    List<VideosVo> findVideoAndMyLikeAll(@Param("userId")String userId);

    // 查询个人关注的人发的作品列表
    List<VideosVo> findVideoAndMyFollowAll(@Param("userId")String userId);

    // 视频表收藏数量 +1
    void addVideoLikeCounts(String videoId);

    // 视频表收藏数量 -1
    void reduceVideoLikeCounts(String videoId);
}