package com.sam.yh.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.EmailAddressValidator;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.req.bean.ResellerBtyInfoReq;
import com.sam.yh.resp.bean.ResellerBtyInfoResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;

@RestController
@RequestMapping("/reseller")
public class ResellerBtyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ResellerBtyInfoController.class);

    @RequestMapping(value = "/btyinfo", method = RequestMethod.POST)
    public SamResponse fetchResellerBtyInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);

        ResellerBtyInfoReq req = JSON.parseObject(jsonReq, ResellerBtyInfoReq.class);

        try {

            validateResellerArgs(req);

            SamResponse resp = new SamResponse();
            ResellerBtyInfoResp respData = new ResellerBtyInfoResp();

            // TODO

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
            logger.error("fetch reseller bty info exception, " + req.getResellerEmail(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    private void validateResellerArgs(ResellerBtyInfoReq fetchResellerBtyInfoReq) throws IllegalParamsException {
        if (!EmailAddressValidator.isValidEmail(fetchResellerBtyInfoReq.getResellerEmail())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }
    }

}
