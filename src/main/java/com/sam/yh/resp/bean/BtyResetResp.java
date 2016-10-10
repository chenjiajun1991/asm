package com.sam.yh.resp.bean;

import java.util.Date;

public class BtyResetResp {
	private boolean isSuccess;
	private Date resetDate;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Date getResetDate() {
		return resetDate;
	}
	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}
}
