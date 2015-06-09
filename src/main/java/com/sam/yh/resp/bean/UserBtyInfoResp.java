package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

public class UserBtyInfoResp {

    private List<UserBtyInfo> btyInfo = new ArrayList<UserBtyInfo>();

    public List<UserBtyInfo> getBtyInfo() {
        return btyInfo;
    }

    public void setBtyInfo(List<UserBtyInfo> btyInfo) {
        this.btyInfo = btyInfo;
    }

}