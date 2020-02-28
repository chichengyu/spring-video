package com.video.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "video_bgm")
public class Bgm {
    @Id
    private String id;

    private String author;

    private String name;

    /**
     * 播放地址
     */
    private String path;

    public Bgm(String id, String author, String name, String path) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.path = path;
    }

    public Bgm() {
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
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取播放地址
     *
     * @return path - 播放地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置播放地址
     *
     * @param path 播放地址
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }
}