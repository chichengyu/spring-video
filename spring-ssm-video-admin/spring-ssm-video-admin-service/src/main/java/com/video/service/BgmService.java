package com.video.service;

import com.video.pojo.Bgm;
import com.video.utils.Response;

import java.util.Map;

public interface BgmService {

    /**
     * 添加 bgm
     * @param bgm
     * @return
     */
    Response<String> addBgm(Bgm bgm);

    /**
     * 查询 bgm 列表数据
     * @param page
     * @param size
     * @return
     */
    Map queryBgmList(Integer page, int size);

    /**
     * 删除音乐
     * @param bgmId
     * @return
     */
    Response<String> deleteBgm(String bgmId);
}
