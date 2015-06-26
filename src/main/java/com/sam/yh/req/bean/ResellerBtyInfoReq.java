package com.sam.yh.req.bean;

public class ResellerBtyInfoReq extends BaseReq {

    private String resellerPhone;

    private int pageNo;
    private int size;

    public String getResellerPhone() {
        return resellerPhone;
    }

    public void setResellerPhone(String resellerPhone) {
        this.resellerPhone = resellerPhone;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
