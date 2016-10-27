package com.sam.yh.model.web;

public class CodeInfoModel {
	    private Integer id;

	    private String mobilePhone;

	    private String dynamicCode;

	    private Integer codeType;

	    private Integer sendTimes;

	    private String sendDate;

	    private String expiryDate;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}

		public String getDynamicCode() {
			return dynamicCode;
		}

		public void setDynamicCode(String dynamicCode) {
			this.dynamicCode = dynamicCode;
		}

		public Integer getCodeType() {
			return codeType;
		}

		public void setCodeType(Integer codeType) {
			this.codeType = codeType;
		}

		public Integer getSendTimes() {
			return sendTimes;
		}

		public void setSendTimes(Integer sendTimes) {
			this.sendTimes = sendTimes;
		}

		public String getSendDate() {
			return sendDate;
		}

		public void setSendDate(String sendDate) {
			this.sendDate = sendDate;
		}

		public String getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;
		}

}
