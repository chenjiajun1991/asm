package com.sam.yh.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.SubmitBtySpecException;
import com.sam.yh.req.bean.IllegalReqParamsException;
import com.sam.yh.req.bean.SubmitBtySpecReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.ResellerService;

@RestController
@RequestMapping("/reseller")
public class SubmitBtySpecController {

    public static final Logger logger = LoggerFactory.getLogger(SubmitBtySpecController.class);

    @Autowired
    ResellerService resellerService;

    @RequestMapping(value = "/btyspec", method = RequestMethod.POST)
    public SamResponse getSalt(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);
        SubmitBtySpecReq req = JSON.parseObject(jsonReq, SubmitBtySpecReq.class);

        try {
            validateBtySpecArgs(req);

            resellerService.submitBtySpec(req);

            return ResponseUtils.getNormalResp(StringUtils.EMPTY);
        } catch (IllegalReqParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("signin exception, " + req.getUserPhone(), e);
            if (e instanceof SubmitBtySpecException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("signin exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateBtySpecArgs(SubmitBtySpecReq submitBtySpecReq) throws IllegalReqParamsException {
        if (!MobilePhoneUtils.isValidPhone(submitBtySpecReq.getUserPhone())) {
            throw new IllegalReqParamsException("请输入购买人正确的手机号码");
        }
        if (!MobilePhoneUtils.isValidPhone(submitBtySpecReq.getResellerPhone())) {
            throw new IllegalReqParamsException("请输入经销商正确的手机号码");
        }
        if (StringUtils.isBlank(submitBtySpecReq.getBtySN())) {
            throw new IllegalReqParamsException("请输入序列号");
        }
        if (StringUtils.isBlank(submitBtySpecReq.getBtyImei())) {
            throw new IllegalReqParamsException("请输入IMEI");
        }
        if (StringUtils.isBlank(submitBtySpecReq.getBtySimNo())) {
            throw new IllegalReqParamsException("请输入sim卡号");
        }
    }

}
