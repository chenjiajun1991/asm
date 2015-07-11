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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchResellerException;
import com.sam.yh.model.Reseller;
import com.sam.yh.req.bean.FetchResellerReq;
import com.sam.yh.resp.bean.ResellerSiteResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.ResellerService;

@RestController
@RequestMapping("/reseller")
public class FetchResellerSiteController {

    private static final Logger logger = LoggerFactory.getLogger(FetchResellerSiteController.class);

    @Autowired
    ResellerService resellerService;

    @RequestMapping(value = "/site", method = RequestMethod.POST)
    public SamResponse fetchResellerInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.info("Request json String:" + jsonReq);
        FetchResellerReq req = JSON.parseObject(jsonReq, FetchResellerReq.class);

        try {
            validateResellerArgs(req);

            Reseller reseller = resellerService.fetchReseller(req.getResellerPhone());
            ResellerSiteResp respData = new ResellerSiteResp();
            respData.setLatitude(reseller.getLatitude());
            respData.setLongitude(reseller.getLongitude());

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("fetch reseller info exception, " + req.getResellerPhone(), e);
            if (e instanceof FetchResellerException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("fetch reseller info exception, " + req.getResellerPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateResellerArgs(FetchResellerReq fetchResellerReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(fetchResellerReq.getResellerPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }
    }
}
