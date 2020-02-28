package com.video.service.impl;

import com.video.dao.UsersDao;
import com.video.dao.UsersFansDao;
import com.video.dao.UsersLikeVideosDao;
import com.video.dao.UsersReportDao;
import com.video.pojo.Users;
import com.video.pojo.UsersFans;
import com.video.pojo.UsersLikeVideos;
import com.video.pojo.UsersReport;
import com.video.service.UserService;
import com.video.utils.IdWorker;
import com.video.utils.JwtTokenUtils;
import com.video.utils.Response;
import com.video.pojo.vo.UsersVo;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final String USER_TOKEN_KEY = "user-redis-token:";// 用户 key
    private final long time = 1000 * 60 * 30;// 过期时间

    @Autowired
    private UsersDao usersDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UsersLikeVideosDao usersLikeVideosDao;
    @Autowired
    private UsersFansDao usersFansDao;
    @Autowired
    private UsersReportDao usersReportDao;

    /**
     * 设置 user token
     * @param userId
     * @param token
     */
    private void setUserTokenForRedis(String userId,String token){
        redisTemplate.boundValueOps(USER_TOKEN_KEY+userId).set(token,time, TimeUnit.SECONDS);
    }

    /**
     * 注册用户
     * @param users
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Response<UsersVo> register(Users users) {
        // 判空
        if(StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())){
            return Response.error("用户名或密码不能为空");
        }
        // 判断是否存在
        if (!findByUsernameIsExist(users.getUsername())){
            return Response.error("用户名已存在");
        }
        users.setId(String.valueOf(idWorker.nextId()));
        users.setPassword(BCrypt.hashpw(users.getPassword(),BCrypt.gensalt()));
        users.setNickname(users.getUsername());
        users.setFollowCounts(0);
        users.setFansCounts(0);
        users.setReceiveLikeCounts(0);

        int resultCount = usersDao.insertSelective(users);
        if (resultCount > 0){
            users.setPassword(StringUtils.EMPTY);// 密码置空
            // 生成 token 并存到 redis
            String token = JwtTokenUtils.getInstance()
                    .setClaim("id", users.getId())
                    .generateToken();
            setUserTokenForRedis(users.getId(),token);
            // 生成 token 并存到 redis
            UsersVo usersVo = new UsersVo();
            BeanUtils.copyProperties(users,usersVo);
            usersVo.settoken(token);
            return Response.success(usersVo);
        }
        return Response.error("注册失败！");
    }

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    @Override
    public Response<UsersVo> findByUsernameAndPasswordForLogin(String username, String password) {
        // 判空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return Response.error("用户名或密码不能为空！");
        }
        /*Users users = new Users();
        users.setUsername(username);
        users.setPassword(password);
        Users reslut = usersDao.selectOne(users);
        */
        Example example = new Example(Users.class);
        example.createCriteria().andEqualTo("username",username);
        Users users = usersDao.selectOneByExample(example);
        if (users == null){
            return Response.error("用户不存在！");
        }
        if (!BCrypt.checkpw(password,users.getPassword())){
            return Response.error("密码不正确, 请重试！");
        }
        users.setPassword(StringUtils.EMPTY);// 密码置空
        // 生成 token 并存到 redis
        String token = JwtTokenUtils.getInstance()
                .setClaim("id", users.getId())
                .generateToken();
        setUserTokenForRedis(users.getId(),token);
        // 设置返回数据
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(users,usersVo);
        usersVo.settoken(token);
        return Response.success(usersVo);
    }

    /**
     * 注销登陆
     * @param userId
     * @return
     */
    @Override
    public Response logout(String userId) {
        if (StringUtils.isBlank(userId)){
            return Response.error("参数错误！");
        }
        // 删除 user token
        redisTemplate.delete(USER_TOKEN_KEY + userId);
        return Response.success();
    }

    /**
     * 更新用户信息
     * @param users
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateUserInfo(Users users) {
        int resultCount = usersDao.updateByPrimaryKeySelective(users);
        if (resultCount > 0){
            return true;
        }
        return false;
    }

    /**
     * 通过 userid 查询用户信息
     * @param userId
     * @return
     */
    @Override
    public Response<UsersVo> findByUserIdForUser(String userId,String fanId) {
        if (StringUtils.isBlank(userId)){
            return Response.error("参数不能为空！");
        }
        Users users = usersDao.selectByPrimaryKey(userId);
        if (users == null){
            return Response.error("参数错误！");
        }
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(users,usersVo);
        usersVo.setFollow(isFollow(userId,fanId));// 是否是粉丝
        return Response.success(usersVo);
    }
    // 查询是否是发布者粉丝
    private boolean isFollow(String userId,String fanId){
        Example example = new Example(UsersFans.class);
        example.createCriteria().andEqualTo("userId",userId).andEqualTo("fanId",fanId);
        return usersFansDao.selectCountByExample(example) > 0;
    }

    /**
     * 通过 userid 查询用户信息
     * @param userId
     * @return
     */
    @Override
    public Users findByIdUser(String userId) {
        return usersDao.selectByPrimaryKey(userId);
    }

    /**
     * 查询当前登录者和视频的点赞关系
     * @param userId
     * @param videoId
     * @return
     */
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
            return false;
        }
        Example example = new Example(UsersLikeVideos.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andEqualTo("videoId",videoId);
        int resultCount = usersLikeVideosDao.selectCountByExample(example);
        if (resultCount > 0){
            return true;
        }
        return false;
    }

    /**
     * 添加关注
     * @param userId
     * @param fanId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<String> saveUserFanRelation(String userId, String fanId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return Response.error("参数错误");
        }
        // 1.添加视频发布者与粉丝的关联表信息
        UsersFans usersFans = new UsersFans();
        usersFans.setId(String.valueOf(idWorker.nextId()));
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
        int addResultCount = usersFansDao.insert(usersFans);
        if (addResultCount > 0){
            // 2.发布者粉丝数量 +1
            usersDao.addFansCount(userId);
            // 3.粉丝关注数量 +1
            usersDao.addFollersCount(fanId);
            return Response.success("关注成功");
        }
        return Response.success("关注失败");
    }

    /**
     * 取消关注
     * @param userId
     * @param fanId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<String> deleteUserFanRelation(String userId, String fanId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)){
            return Response.error("参数错误");
        }
        // 1.删除视频发布者与粉丝的关联表信息
        Example example = new Example(UsersFans.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andEqualTo("fanId",fanId);
        int delResultCount = usersFansDao.deleteByExample(example);
        if (delResultCount > 0){
            // 2.发布者粉丝数量 -1
            usersDao.reduceFansCount(userId);
            // 3.粉丝关注数量 -1
            usersDao.reduceFollersCount(fanId);
            return Response.success("取消关注");
        }
        return Response.error();
    }

    /**
     * 用户举报
     * @param usersReport
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Response<String> saveReportUser(UsersReport usersReport) {
        usersReport.setId(String.valueOf(idWorker.nextId()));
        usersReport.setCreateDate(new Date());
        if (usersReportDao.insert(usersReport) > 0){
            return Response.success();
        }
        return Response.error();
    }

    /**
     * 判断用户是否存在
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean findByUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = usersDao.selectOne(users);
        return result == null;
    }
}
