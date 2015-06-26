package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

public class ResellersResp {

    private int total;
    private List<ResellerInfo> resellers = new ArrayList<ResellerInfo>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResellerInfo> getResellers() {
        return resellers;
    }

    public void setResellers(List<ResellerInfo> resellers) {
        this.resellers = resellers;
    }

}
