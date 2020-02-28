package com.video.dao;

import com.video.pojo.Videos;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface VideosDao extends Mapper<Videos>, MySqlMapper<Videos> {
}