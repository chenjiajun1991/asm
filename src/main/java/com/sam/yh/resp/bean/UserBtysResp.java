package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

import com.sam.yh.model.PubBattery;

public class UserBtysResp {
    
    private List<PubBattery> myBtys = new ArrayList<PubBattery>();
    private List<PubBattery> friendBtys = new ArrayList<PubBattery>();
    
    public List<PubBattery> getMyBtys() {
        return myBtys;
    }
    public void setMyBtys(List<PubBattery> myBtys) {
        this.myBtys = myBtys;
    }
    public List<PubBattery> getFriendBtys() {
        return friendBtys;
    }
    public void setFriendBtys(List<PubBattery> friendBtys) {
        this.friendBtys = friendBtys;
    }
    
}
