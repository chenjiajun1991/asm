package com.sam.yh.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.req.bean.web.UpdateBatteryReq;
import com.sam.yh.req.bean.web.UpdateBtyResellerReq;
import com.sam.yh.req.bean.web.UpdateUserBatteryReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.UpdateBatteryResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/bty")
public class UpdateBtyInfoController {
	@Autowired
    WebService webService;
	 private static final Logger logger = LoggerFactory.getLogger(UpdateBtyInfoController.class);
	 @RequestMapping(value = "/updatebty", method = RequestMethod.POST)
	    public SamResponse UpdateBattery(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        UpdateBatteryReq req = JSON.parseObject(jsonReq, UpdateBatteryReq.class);
	        try {
	    	
	        	webService.updateBattery(req.getBtyId(), req.getBtyImei(), req.getBtySimNo(), req.getBtySn(), req.getBtyQuantity());
	            UpdateBatteryResp respData = new UpdateBatteryResp();
	            respData.setUpdateBtyResp("ok");
	       
	            return ResponseUtils.getNormalResp(respData);
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("updateBty exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	 }
	 
	 @RequestMapping(value = "/updateuserbty", method = RequestMethod.POST)
	    public SamResponse UpdateUserBattery(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        UpdateUserBatteryReq req = JSON.parseObject(jsonReq, UpdateUserBatteryReq.class);
	        try {
	        	webService.updateUserBattery(req.getBtyId(), req.getUserName(), req.getUserPhone());
	            UpdateBatteryResp respData = new UpdateBatteryResp();
	            respData.setUpdateBtyResp("ok");
	       
	            return ResponseUtils.getNormalResp(respData);
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("updateUserBattery exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	 }
	 
	 @RequestMapping(value = "/updatebtyreseller", method = RequestMethod.POST)
	    public SamResponse UpdateBatteryReseller(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        UpdateBtyResellerReq req = JSON.parseObject(jsonReq, UpdateBtyResellerReq.class);
	        try {
	        	webService.updateBtyReseller(req.getBtyId(), req.getResellerName(), req.getResellerPhone());
	            UpdateBatteryResp respData = new UpdateBatteryResp();
	            respData.setUpdateBtyResp("ok");
	       
	            return ResponseUtils.getNormalResp(respData);
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("updateBtyReseller exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	 }
}
