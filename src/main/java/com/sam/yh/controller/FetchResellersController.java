package com.sam.yh.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.req.bean.FetchResellersReq;
import com.sam.yh.resp.bean.ResellersResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.ResellerService;

@RestController
public class FetchResellersController {

    private static final Logger logger = LoggerFactory.getLogger(FetchResellersController.class);

    @Autowired
    ResellerService resellerService;

    @RequestMapping(value = "/resellers", method = RequestMethod.POST)
    public SamResponse loggingReseller(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);
        FetchResellersReq req = JSON.parseObject(jsonReq, FetchResellersReq.class);

        try {
            validateAdminArgs(req);

            // TODO
            ResellersResp respData = new ResellersResp();

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
            logger.error("fetch resellers exception, " + req.getAdminPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateAdminArgs(FetchResellersReq fetchResellersReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(fetchResellersReq.getAdminPhone())) {
            throw new IllegalParamsException("请输入经销商正确的电子邮箱");
        }
    }
}
