package com.video.dao;

import com.video.pojo.UsersReport;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UsersReportDao extends Mapper<UsersReport>, MySqlMapper<UsersReport> {
}