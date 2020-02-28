package com.video.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@ApiModel(value = "视频留言对象",description = "用户留言视频的对象")
@Table(name = "video_comments")
public class CommentsVo {
    private String faceImage;
    private String nickname;
    private String timeAgoStr;
    private String toNickname;

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }

    public String getTimeAgoStr() {
        return timeAgoStr;
    }

    public void setTimeAgoStr(String timeAgoStr) {
        this.timeAgoStr = timeAgoStr;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @ApiModelProperty(hidden = true)
    @Id
    private String id;

    @Column(name = "father_comment_id")
    private String fatherCommentId;

    @Column(name = "to_user_id")
    private String toUserId;

    /**
     * 视频id
     */
    @ApiModelProperty(value = "视频id",name = "videoId",required = true)
    @Column(name = "video_id")
    private String videoId;

    /**
     * 留言者，评论的用户id
     */
    @ApiModelProperty(value = "评论者id",name = "fromUserId",required = true)
    @Column(name = "from_user_id")
    private String fromUserId;

    @ApiModelProperty(value = "评论事件",name = "createTime",required = false)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容",name = "comment",required = true)
    private String comment;

    public CommentsVo(String id, String fatherCommentId, String toUserId, String videoId, String fromUserId, Date createTime, String comment) {
        this.id = id;
        this.fatherCommentId = fatherCommentId;
        this.toUserId = toUserId;
        this.videoId = videoId;
        this.fromUserId = fromUserId;
        this.createTime = createTime;
        this.comment = comment;
    }

    public CommentsVo() {
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
     * @return father_comment_id
     */
    public String getFatherCommentId() {
        return fatherCommentId;
    }

    /**
     * @param fatherCommentId
     */
    public void setFatherCommentId(String fatherCommentId) {
        this.fatherCommentId = fatherCommentId == null ? null : fatherCommentId.trim();
    }

    /**
     * @return to_user_id
     */
    public String getToUserId() {
        return toUserId;
    }

    /**
     * @param toUserId
     */
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId == null ? null : toUserId.trim();
    }

    /**
     * 获取视频id
     *
     * @return video_id - 视频id
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * 设置视频id
     *
     * @param videoId 视频id
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId == null ? null : videoId.trim();
    }

    /**
     * 获取留言者，评论的用户id
     *
     * @return from_user_id - 留言者，评论的用户id
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     * 设置留言者，评论的用户id
     *
     * @param fromUserId 留言者，评论的用户id
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId == null ? null : fromUserId.trim();
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取评论内容
     *
     * @return comment - 评论内容
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置评论内容
     *
     * @param comment 评论内容
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}