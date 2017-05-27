package com.sam.yh.resp.bean;

import org.apache.commons.lang3.StringUtils;

public class ResponseUtils {

    public static final String RESCODE_SUCCESS = "10000";
    public static final String RESCODE_PARAMS_EXCEPTION = "10001";
    public static final String RESCODE_SERVICE_EXCEPTION = "10002";
    public static final String FORCE_UPDATE = "10003";
    public static final String OPTIONAL_UPDATE = "10004";
    public static final String RESCODE_PROCESS_ERROR = "10009";
    public static final String RESCODE_UNKNOW_EXCEPTION = "10099";
    public static String AndroidApkVersion;
    public static String AndroidApkDownloadUrl;

    public static SamResponse getNormalResp(String result) {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_SUCCESS);
        resp.setResult(StringUtils.isBlank(result) ? StringUtils.EMPTY : result);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getNormalResp(Object respObj) {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_SUCCESS);
        resp.setResult(StringUtils.EMPTY);
        resp.setData(respObj == null ? StringUtils.EMPTY : respObj);

        return resp;
    }

    public static SamResponse getNormalResp(String result, Object respObj) {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_SUCCESS);
        resp.setResult(StringUtils.isBlank(result) ? StringUtils.EMPTY : result);
        resp.setData(respObj == null ? StringUtils.EMPTY : respObj);

        return resp;
    }

    public static SamResponse getErrorResp(String message) {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_PROCESS_ERROR);
        resp.setResult(message);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getParamsErrorResp(String message) {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_PARAMS_EXCEPTION);
        resp.setResult(message);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getServiceErrorResp(String message) {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_SERVICE_EXCEPTION);
        resp.setResult(message);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getSysErrorResp() {
        SamResponse resp = new SamResponse();
        resp.setResCode(RESCODE_UNKNOW_EXCEPTION);
        resp.setResult("未知异常");
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getForceUpdateResp(Object respObj) {
        SamResponse resp = new SamResponse();
        resp.setResCode(FORCE_UPDATE);
        resp.setResult("存在强制更新");
        resp.setData(respObj);

        return resp;
    }

    public static SamResponse getOptionalUpdateresp(Object respObj) {
        SamResponse resp = new SamResponse();
        resp.setResCode(OPTIONAL_UPDATE);
        resp.setResult("存在可选更新");
        resp.setData(respObj);

        return resp;
    }
    
    //test
    public static SendBtyInfo getSendResp(Object respObj){
    	SendBtyInfo resp = new SendBtyInfo();
    	resp.setMsg(respObj);
    	return resp;
    }

}
