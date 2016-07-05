package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;
//修改了经销商查询所有电池的销售信息
public class ResellerBtyInfoResp {

    private int total;
    private List<ResellerUserBtyInfo> btyInfo = new ArrayList<ResellerUserBtyInfo>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

	public List<ResellerUserBtyInfo> getBtyInfo() {
		return btyInfo;
	}

	public void setBtyInfo(List<ResellerUserBtyInfo> btyInfo) {
		this.btyInfo = btyInfo;
	}


}