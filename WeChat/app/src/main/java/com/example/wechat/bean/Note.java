package com.example.wechat.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Note implements Serializable {
    private String title;
    private Date changetime;
    private String detail;
    private UUID uuid;
    private String detailHtmlString;
    private int deleted;
    public Note(UUID uuid, String title, String detail, Date changetime, String detailHtmlString, int deleted) {
        this.title = title;
        this.changetime = changetime;
        this.detail = detail;
        this.uuid = uuid;
        this.detailHtmlString = detailHtmlString;
        this.deleted = deleted;
    }

    public Note()
    {
        this.uuid = UUID.randomUUID();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getChangetime() {
        return changetime;
    }

    public void setChangetime(Date changetime) {
        this.changetime = changetime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDetailHtmlString() {
        return detailHtmlString;
    }

    public void setDetailHtmlString(String htmlString) {
        this.detailHtmlString = htmlString;
    }

    public int isDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
