package com.sam.yh.model;

import java.util.Date;

public class UserFollow extends UserFollowKey {

    private String btyPubSn;

    private String bytImei;

    private Boolean followStatus;

    private Date followDate;

    public String getBtyPubSn() {
        return btyPubSn;
    }

    public void setBtyPubSn(String btyPubSn) {
        this.btyPubSn = btyPubSn == null ? null : btyPubSn;
    }

    public String getBytImei() {
        return bytImei;
    }

    public void setBytImei(String bytImei) {
        this.bytImei = bytImei;
    }

    public Boolean getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Boolean followStatus) {
        this.followStatus = followStatus;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }
}