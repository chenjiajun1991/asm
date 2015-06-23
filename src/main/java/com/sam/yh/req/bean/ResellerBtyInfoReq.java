package com.sam.yh.req.bean;

public class ResellerBtyInfoReq extends BaseReq {

    private String resellerEmail;

    private int offset;
    private int limit;

    public String getResellerEmail() {
        return resellerEmail;
    }

    public void setResellerEmail(String resellerEmail) {
        this.resellerEmail = resellerEmail;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
