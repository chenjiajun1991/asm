package com.sam.yh.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.AppVersionUtils;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.enums.AppVersionStatus;
import com.sam.yh.req.bean.ResellerBtyInfoReq;
import com.sam.yh.resp.bean.ResellerBtyInfo;
import com.sam.yh.resp.bean.ResellerBtyInfoResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.ResellerService;

@RestController
@RequestMapping("/reseller")
public class ResellerBtyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ResellerBtyInfoController.class);

    @Autowired
    ResellerService resellerService;

    @RequestMapping(value = "/btyinfo", method = RequestMethod.POST)
    public SamResponse fetchResellerBtyInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        ResellerBtyInfoReq req = JSON.parseObject(jsonReq, ResellerBtyInfoReq.class);

        try {
            AppVersionStatus verStatus = AppVersionUtils.checkVersion(req);
            if (StringUtils.equals(AppVersionStatus.FORCE_UPDATE.getStatus(), verStatus.getStatus())) {
                return ResponseUtils.getForceUpdateResp();
            }

            validateResellerArgs(req);

            // TODO
            List<ResellerBtyInfo> reslut = resellerService.fetchResellerBtyInfo(req.getResellerPhone(), req.getPageNo(), req.getSize());

            ResellerBtyInfoResp respData = new ResellerBtyInfoResp();
            respData.setBtyInfo(reslut);
            respData.setTotal(resellerService.countSoldBtys(req.getResellerPhone()));
            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("fetch reseller bty info exception, " + req.getResellerPhone(), e);
            return ResponseUtils.getServiceErrorResp(e.getMessage());
        } catch (Exception e) {
            logger.error("fetch reseller bty info exception, " + req.getResellerPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    private void validateResellerArgs(ResellerBtyInfoReq fetchResellerBtyInfoReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(fetchResellerBtyInfoReq.getResellerPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }
    }

}
