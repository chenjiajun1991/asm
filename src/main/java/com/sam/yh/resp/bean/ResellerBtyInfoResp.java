package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

public class ResellerBtyInfoResp {

    private int total;
    private List<ResellerBtyInfo> btyInfo = new ArrayList<ResellerBtyInfo>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResellerBtyInfo> getBtyInfo() {
        return btyInfo;
    }

    public void setBtyInfo(List<ResellerBtyInfo> btyInfo) {
        this.btyInfo = btyInfo;
    }

}