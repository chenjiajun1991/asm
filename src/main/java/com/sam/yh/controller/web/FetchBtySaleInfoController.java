package com.sam.yh.controller.web;


import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
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
import com.google.common.collect.Sets;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.controller.BtySaleInfoController;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.web.BtySaleInfoModel;
import com.sam.yh.req.bean.web.FetchAllSaleInfoReq;
import com.sam.yh.req.bean.web.FetchBtyByImeiReq;
import com.sam.yh.req.bean.web.FetchBtyByPhoneReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.FetchAllSaleInfoResp;
import com.sam.yh.resp.bean.web.FetchBtyByImeiResp;
import com.sam.yh.resp.bean.web.FetchBtyByPhoneResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/bty")
public class FetchBtySaleInfoController {
	@Autowired
    WebService webService;
	
	@Resource
	private String adminPhones;

    private static final Logger logger = LoggerFactory.getLogger(BtySaleInfoController.class);

    @RequestMapping(value = "/detelinfo", method = RequestMethod.POST)
    public SamResponse signin(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);
        FetchAllSaleInfoReq req = JSON.parseObject(jsonReq, FetchAllSaleInfoReq.class);
        try {
        	validateArgs(req);
        	
            List<BtySaleInfoModel> btySaleInfos = webService.fetchAllSaleInfo();
 
         	FetchAllSaleInfoResp respData =new FetchAllSaleInfoResp();
            respData.setBtySaleInfos(btySaleInfos);
       
            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
        	if(e instanceof CrudException){
        		 logger.error("fetchBatteryAllSaleinfo exception, " +  e);
        		return ResponseUtils.getServiceErrorResp(e.getMessage());
        	}else{
        		return ResponseUtils.getSysErrorResp();
        	}
           
        }
    }
    
    @RequestMapping(value = "/byimei", method = RequestMethod.POST)
    public SamResponse fetchBtyByImei(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);
        FetchBtyByImeiReq req = JSON.parseObject(jsonReq, FetchBtyByImeiReq.class);
        try {
    	
        	BtySaleInfoModel btySaleInfo=webService.fetBtyByImei(req.getImei());
          
         	FetchBtyByImeiResp respData = new FetchBtyByImeiResp();
         	
            respData.setBtySaleInfo(btySaleInfo);
       
            return ResponseUtils.getNormalResp(respData);
        }  catch (Exception e) {
        	if(e instanceof CrudException){
        		 logger.error("fetchBatteryAllSaleinfo exception, " +  e);
        		return ResponseUtils.getServiceErrorResp(e.getMessage());
        	}else{
        		return ResponseUtils.getSysErrorResp();
        	}
           
        }
    }
    
    @RequestMapping(value = "/byphone", method = RequestMethod.POST)
    public SamResponse fetchBtyByPhone(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);
        FetchBtyByPhoneReq req = JSON.parseObject(jsonReq, FetchBtyByPhoneReq.class);
        try {
    	
        	List<BtySaleInfoModel> btySaleInfos = webService.fetchBtyByPhone(req.getMobilePhone());
          
         	FetchBtyByPhoneResp respData = new FetchBtyByPhoneResp();
         	
            respData.setBtySaleInfos(btySaleInfos);
       
            return ResponseUtils.getNormalResp(respData);
        }  catch (Exception e) {
        	if(e instanceof CrudException){
        		 logger.error("fetchBatteryAllSaleinfo exception, " +  e);
        		return ResponseUtils.getServiceErrorResp(e.getMessage());
        	}else{
        		return ResponseUtils.getSysErrorResp();
        	}
           
        }
    }
    
    
    
    private void validateArgs(FetchAllSaleInfoReq fetchAllSaleInfoReq) throws IllegalParamsException {
        if (!isAdminPhone(fetchAllSaleInfoReq.getAccount())) {
            throw new IllegalParamsException("你无权查看销售信息！");
        } 
    }
    
    private boolean isAdminPhone(String userPhone) {
        String[] phones = StringUtils.split(adminPhones, ",");
        if (phones == null || phones.length == 0) {
            logger.error("获取admin phone 失败");
        }
        Set<String> admins = Sets.newHashSet(phones);
        return admins.contains(userPhone);
    }
   
}
