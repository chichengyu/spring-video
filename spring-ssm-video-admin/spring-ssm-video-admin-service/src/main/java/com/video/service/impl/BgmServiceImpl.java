package com.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.video.dao.BgmDao;
import com.video.pojo.Bgm;
import com.video.service.BgmService;
import com.video.utils.IdWorker;
import com.video.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BgmDao bgmDao;

    /**
     * 添加 bgm
     * @param bgm
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<String> addBgm(Bgm bgm) {
        bgm.setId(String.valueOf(idWorker.nextId()));
        int resultCount = bgmDao.insert(bgm);
        if (resultCount > 0){
            return Response.success("添加成功！");
        }
        return Response.error("添加失败！");
    }

    /**
     * 查询 bgm 列表数据
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map queryBgmList(Integer page, int size) {
        PageHelper.startPage(page,size);
        List<Bgm> bgms = bgmDao.selectAll();
        PageInfo<Bgm> pageInfo = new PageInfo<Bgm>(bgms);
        Map map = new HashMap();
        map.put("page",pageInfo.getPageNum());
        map.put("rows",pageInfo.getList());
        map.put("total",pageInfo.getPages());
        map.put("records",pageInfo.getTotal());
        return map;
    }

    /**
     * 删除音乐
     * @param bgmId
     * @return
     */
    @Override
    public Response<String> deleteBgm(String bgmId) {
        if (StringUtils.isBlank(bgmId)){
            return Response.error("参数错误！");
        }
        int resultCount = bgmDao.deleteByPrimaryKey(bgmId);
        if (resultCount > 0){
            return Response.success();
        }
        return Response.error();
    }
}
