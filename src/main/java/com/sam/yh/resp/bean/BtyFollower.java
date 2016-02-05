package com.sam.yh.resp.bean;

import java.io.Serializable;

public class BtyFollower implements Serializable {

    private static final long serialVersionUID = 1L;
    private String followerName;
    private String followerPhone;

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowerPhone() {
        return followerPhone;
    }

    public void setFollowerPhone(String followerPhone) {
        this.followerPhone = followerPhone;
    }

}
