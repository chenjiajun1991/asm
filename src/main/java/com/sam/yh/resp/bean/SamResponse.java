package com.sam.yh.resp.bean;

public class SamResponse {

    public static final String STATUS_SUCCESS = "0";

    public static final String RESCODE_SUCCESS = "10000";
    public static final String RESCODE_PARAMS_EXCEPTION = "10001";
    public static final String RESCODE_UNKNOW_EXCEPTION = "10099";

    private String status;
    private String result;
    private String resCode;
    private Object data;

    public SamResponse() {
        this.status = STATUS_SUCCESS;
        this.result = "";
        this.resCode = RESCODE_SUCCESS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
