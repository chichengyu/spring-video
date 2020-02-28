package com.video.service;

import com.video.pojo.Bgm;
import com.video.utils.Response;

import java.util.List;

public interface BgmService {

    /**
     * 查询 bgm 列表
     * @return
     */
    Response<List<Bgm>> findBgmList();

    /**
     * 通过 bgmid 查询
     * @param bgmId
     * @return
     */
    Bgm findByBgmId(String bgmId);
}
