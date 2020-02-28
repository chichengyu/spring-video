package com.video.service;

import com.video.pojo.Users;
import com.video.pojo.UsersReport;
import com.video.utils.Response;
import com.video.pojo.vo.UsersVo;

public interface UserService {

    /**
     * 判断用户是否存在
     * @param username
     * @return
     */
    boolean findByUsernameIsExist(String username);

    /**
     * 注册用户
     * @param users
     */
    Response<UsersVo> register(Users users);

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    Response<UsersVo> findByUsernameAndPasswordForLogin(String username, String password);

    /**
     * 注销登陆
     * @param userId
     * @return
     */
    Response logout(String userId);

    /**
     * 更新用户信息
     * @param users
     * @return
     */
    boolean updateUserInfo(Users users);

    /**
     * 通过 userid 查询用户信息
     * @param userId
     * @return
     */
    Response<UsersVo> findByUserIdForUser(String userId,String fanId);

    /**
     * 通过 userid 查询用户信息
     * @param userId
     * @return
     */
    Users findByIdUser(String userId);

    /**
     * 查询当前登录者和视频的点赞关系
     * @param userId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 添加关注
     * @param userId
     * @param fanId
     * @return
     */
    Response<String> saveUserFanRelation(String userId, String fanId);

    /**
     * 取消关注
     * @param userId
     * @param fanId
     * @return
     */
    Response<String> deleteUserFanRelation(String userId, String fanId);

    /**
     * 用户举报
     * @param usersReport
     * @return
     */
    Response<String> saveReportUser(UsersReport usersReport);
}
