package com.video.enums;

/**
 * 视频状态
 */
public enum VideoStatusEnum{
    SUCCESS(1,"发布成功"),
    FORBID(2,"禁止播放");// 禁止播放，管理员操作;

    private Integer code;
    private String message;

    VideoStatusEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
