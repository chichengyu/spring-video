package com.video.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "video_users_like_videos")
public class UsersLikeVideos {
    @Id
    private String id;

    /**
     * 用户
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 视频
     */
    @Column(name = "video_id")
    private String videoId;

    public UsersLikeVideos(String id, String userId, String videoId) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
    }

    public UsersLikeVideos() {
        super();
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取用户
     *
     * @return user_id - 用户
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户
     *
     * @param userId 用户
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取视频
     *
     * @return video_id - 视频
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * 设置视频
     *
     * @param videoId 视频
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId == null ? null : videoId.trim();
    }
}