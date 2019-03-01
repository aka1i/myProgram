package com.example.wechat.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Schedule implements Serializable {
    private String title;
    private Date deadLine;
    private String detail;
    private UUID uuid;
    private int hasRemind;

    public Schedule(UUID uuid,String title, String detail, Date deadLine) {
        this.uuid = uuid;
        this.title = title;
        this.deadLine = deadLine;
        this.detail = detail;
    }
    public Schedule(){
        this.uuid = UUID.randomUUID();
    }
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int isHasRemind() {
        return hasRemind;
    }

    public void setHasRemind(int hasRemaind) {
        this.hasRemind = hasRemaind;
    }
}
