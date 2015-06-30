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
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.SubmitBtySpecException;
import com.sam.yh.req.bean.LogResellerReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.ResellerService;

@RestController
@RequestMapping("/reseller")
public class LogResellerController {

    private static final Logger logger = LoggerFactory.getLogger(LogResellerController.class);

    @Autowired
    ResellerService resellerService;

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public SamResponse logReseller(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);
        LogResellerReq req = JSON.parseObject(jsonReq, LogResellerReq.class);

        try {
            validateResellerArgs(req);

            resellerService.logReseller(req);

            return ResponseUtils.getNormalResp(StringUtils.EMPTY);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("logging reseller exception, " + req.getResellerPhone(), e);
            if (e instanceof SubmitBtySpecException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("logging reseller exception, " + req.getResellerPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateResellerArgs(LogResellerReq logResellerReq) throws IllegalParamsException {
        if (StringUtils.isBlank(logResellerReq.getResellerName())) {
            throw new IllegalParamsException("请输入经销商名称");
        }
        if (!MobilePhoneUtils.isValidPhone(logResellerReq.getResellerPhone())) {
            throw new IllegalParamsException("请输入经销商正确的电子邮箱");
        }
        if (StringUtils.isBlank(logResellerReq.getCityName())) {
            throw new IllegalParamsException("请输入经销商所在城市");
        }
        if (StringUtils.isBlank(logResellerReq.getResellerAddress())) {
            throw new IllegalParamsException("请输入经销商地址");
        }
    }

}
