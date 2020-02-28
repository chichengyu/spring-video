package com.video.dao;

import com.video.pojo.Users;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UsersDao extends Mapper<Users>, MySqlMapper<Users> {
}