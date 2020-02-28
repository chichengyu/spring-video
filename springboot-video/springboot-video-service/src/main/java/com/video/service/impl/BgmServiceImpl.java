package com.video.service.impl;

import com.video.dao.BgmDao;
import com.video.pojo.Bgm;
import com.video.service.BgmService;
import com.video.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmDao bgmDao;

    /**
     * 查询 bgm 列表
     * @return
     */
    @Override
    public Response<List<Bgm>> findBgmList(){
        return Response.success(bgmDao.selectAll());
    }

    /**
     * 通过 bgmid 查询
     * @param bgmId
     * @return
     */
    @Override
    public Bgm findByBgmId(String bgmId) {
        return bgmDao.selectByPrimaryKey(bgmId);
    }
}
