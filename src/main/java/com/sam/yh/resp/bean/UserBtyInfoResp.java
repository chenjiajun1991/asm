package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

public class UserBtyInfoResp {

    private List<UserBtyInfo> selfBtyInfo = new ArrayList<UserBtyInfo>();
    private List<UserBtyInfo> friendsBtyInfo = new ArrayList<UserBtyInfo>();

    public List<UserBtyInfo> getSelfBtyInfo() {
        return selfBtyInfo;
    }

    public void setSelfBtyInfo(List<UserBtyInfo> selfBtyInfo) {
        this.selfBtyInfo = selfBtyInfo;
    }

    public List<UserBtyInfo> getFriendsBtyInfo() {
        return friendsBtyInfo;
    }

    public void setFriendsBtyInfo(List<UserBtyInfo> friendsBtyInfo) {
        this.friendsBtyInfo = friendsBtyInfo;
    }

}