package com.sam.yh.model;

import java.util.Date;

public class UserFollow extends UserFollowKey {
    private Boolean followStatus;

    private Date followDate;

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