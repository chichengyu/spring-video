package com.video.dao;

import com.video.pojo.Comments;
import com.video.pojo.vo.CommentsVo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface CommentsDao extends Mapper<Comments>, MySqlMapper<Comments> {
    /**
     * 获取评论分页列表
     * @param videoId
     * @return
     */
    List<CommentsVo> findCommentsAll(String videoId);
}