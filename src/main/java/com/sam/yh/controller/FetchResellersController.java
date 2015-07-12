package com.sam.yh.controller;

import java.util.List;

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
import com.sam.yh.crud.exception.NotAdminException;
import com.sam.yh.req.bean.FetchResellersReq;
import com.sam.yh.resp.bean.ResellerInfo;
import com.sam.yh.resp.bean.ResellersResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.ResellerService;

@RestController
@RequestMapping("/reseller")
public class FetchResellersController {

    private static final Logger logger = LoggerFactory.getLogger(FetchResellersController.class);

    @Autowired
    ResellerService resellerService;

    @Autowired
    BatteryService batteryService;

    @RequestMapping(value = "/infos", method = RequestMethod.POST)
    public SamResponse fetchResellers(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.info("Request json String:" + jsonReq);
        FetchResellersReq req = JSON.parseObject(jsonReq, FetchResellersReq.class);

        try {
            validateAdminArgs(req);

            // TODO
            List<ResellerInfo> result = resellerService.fetchResellers(req.getAdminPhone(), req.getPageNo(), req.getSize());
            for (ResellerInfo resellerInfo : result) {
                resellerInfo.setSoldCount(batteryService.countSoldBtys(resellerInfo.getResellerId()));
            }
            ResellersResp respData = new ResellersResp();
            respData.setTotal(resellerService.countResellers());
            respData.setResellers(result);

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("fetch resellers exception, " + req.getAdminPhone(), e);
            if (e instanceof NotAdminException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("fetch resellers exception, " + req.getAdminPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateAdminArgs(FetchResellersReq fetchResellersReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(fetchResellersReq.getAdminPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }
    }
}
