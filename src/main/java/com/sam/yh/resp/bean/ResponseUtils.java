package com.sam.yh.resp.bean;

import org.apache.commons.lang3.StringUtils;

public class ResponseUtils {

    public static final String STATUS_SUCCESS = "0";

    public static final String RESCODE_SUCCESS = "10000";
    public static final String RESCODE_PARAMS_EXCEPTION = "10001";
    public static final String RESCODE_SERVICE_EXCEPTION = "10002";
    public static final String RESCODE_UNKNOW_EXCEPTION = "10099";

    public static SamResponse getNormalResp(String result) {
        SamResponse resp = new SamResponse();
        resp.setStatus(STATUS_SUCCESS);
        resp.setResCode(RESCODE_SUCCESS);
        resp.setResult(StringUtils.isBlank(result) ? StringUtils.EMPTY : result);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getNormalResp(Object respObj) {
        SamResponse resp = new SamResponse();
        resp.setStatus(STATUS_SUCCESS);
        resp.setResCode(RESCODE_SUCCESS);
        resp.setResult(StringUtils.EMPTY);
        resp.setData(respObj == null ? StringUtils.EMPTY : respObj);

        return resp;
    }

    public static SamResponse getNormalResp(String result, Object respObj) {
        SamResponse resp = new SamResponse();
        resp.setStatus(STATUS_SUCCESS);
        resp.setResCode(RESCODE_SUCCESS);
        resp.setResult(StringUtils.isBlank(result) ? StringUtils.EMPTY : result);
        resp.setData(respObj == null ? StringUtils.EMPTY : respObj);

        return resp;
    }

    public static SamResponse getParamsErrorResp(String message) {
        SamResponse resp = new SamResponse();
        resp.setStatus(STATUS_SUCCESS);
        resp.setResCode(RESCODE_PARAMS_EXCEPTION);
        resp.setResult(message);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getServiceErrorResp(String message) {
        SamResponse resp = new SamResponse();
        resp.setStatus(STATUS_SUCCESS);
        resp.setResCode(RESCODE_SERVICE_EXCEPTION);
        resp.setResult(message);
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

    public static SamResponse getSysErrorResp() {
        SamResponse resp = new SamResponse();
        resp.setStatus(STATUS_SUCCESS);
        resp.setResCode(RESCODE_UNKNOW_EXCEPTION);
        resp.setResult("未知异常");
        resp.setData(StringUtils.EMPTY);

        return resp;
    }

}
