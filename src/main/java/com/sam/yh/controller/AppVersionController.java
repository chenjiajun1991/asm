package com.sam.yh.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.AppVersionCheckService;
import com.sam.yh.enums.AppVersionStatus;
import com.sam.yh.req.bean.BaseReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.UpdateResp;

@RestController
@RequestMapping("/open")
public class AppVersionController {

    private static final Logger logger = LoggerFactory.getLogger(AppVersionController.class);

    @Resource
    private String apkVersion;

    @Resource
    private String apkDownloadUrl;

    @Resource
    AppVersionCheckService appVersionCheckService;

    @RequestMapping(value = "/ver", method = RequestMethod.POST)
    public SamResponse versionCheck(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        BaseReq req = JSON.parseObject(jsonReq, BaseReq.class);
        try {
            AppVersionStatus verStatus = appVersionCheckService.checkVersion(req);

            UpdateResp respObj = new UpdateResp();
            respObj.setLatestVer(apkVersion);
            respObj.setDownloadUrl(apkDownloadUrl);
            if (StringUtils.equals(AppVersionStatus.FORCE_UPDATE.getStatus(), verStatus.getStatus())) {
                return ResponseUtils.getForceUpdateResp(respObj);
            } else if (StringUtils.equals(AppVersionStatus.OPTIONAL_UPDATE.getStatus(), verStatus.getStatus())) {
                return ResponseUtils.getOptionalUpdateresp(respObj);
            } else {
                return ResponseUtils.getNormalResp("");
            }

        } catch (Exception e) {
            logger.error("app version check exception,", e);
            return ResponseUtils.getErrorResp("版本检测异常");
        }
    }
}
