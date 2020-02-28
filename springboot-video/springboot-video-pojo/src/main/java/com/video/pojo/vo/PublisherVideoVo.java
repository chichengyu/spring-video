package com.video.pojo.vo;

public class PublisherVideoVo {

    private UsersVo publisher;

    private boolean userLikeVideo;

    public PublisherVideoVo() {
    }

    public UsersVo getPublisher() {
        return publisher;
    }

    public void setPublisher(UsersVo publisher) {
        this.publisher = publisher;
    }

    public boolean isUserLikeVideo() {
        return userLikeVideo;
    }

    public void setUserLikeVideo(boolean userLikeVideo) {
        this.userLikeVideo = userLikeVideo;
    }
}
