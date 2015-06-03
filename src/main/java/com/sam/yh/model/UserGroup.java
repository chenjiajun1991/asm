package com.sam.yh.model;

import java.util.Date;

public class UserGroup extends UserGroupKey {
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}