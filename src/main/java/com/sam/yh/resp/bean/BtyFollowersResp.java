package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

public class BtyFollowersResp {

    private List<BtyFollower> followers = new ArrayList<BtyFollower>();

    public List<BtyFollower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<BtyFollower> followers) {
        this.followers = followers;
    }

}
