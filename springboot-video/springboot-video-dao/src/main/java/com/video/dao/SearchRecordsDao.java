package com.video.dao;

import com.video.pojo.SearchRecords;
import com.video.utils.Response;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface SearchRecordsDao extends Mapper<SearchRecords>, MySqlMapper<SearchRecords> {

    /**
     * 热搜词统计查询
     * @return
     */
    List<String> findHotWords();
}