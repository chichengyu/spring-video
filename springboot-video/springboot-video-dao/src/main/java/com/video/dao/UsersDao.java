package com.video.dao;

import com.video.pojo.Users;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UsersDao extends Mapper<Users>, MySqlMapper<Users> {

    // 户表视频发布者被收藏数量 +1
    void addReceiveLikeCounts(String videoCreateUserId);

    // 户表视频发布者被收藏数量 -1
    void reduceReceiveLikeCounts(String videoCreateUserId);

    // 发布者粉丝数量 +1
    void addFansCount(String userId);

    // 发布者粉丝数量 -1
    void reduceFansCount(String userId);

    // 粉丝关注数量 +1
    void addFollersCount(String userId);

    // 粉丝关注数量 -1
    void reduceFollersCount(String userId);
}