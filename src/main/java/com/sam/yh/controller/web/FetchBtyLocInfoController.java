package com.sam.yh.controller.web;

import java.util.List;
import java.util.Set;

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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.Battery;
import com.sam.yh.model.User;
import com.sam.yh.model.web.BatteryLocInfo;
import com.sam.yh.req.bean.web.FetchLocInfoReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.FetchLocInfoResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/bty")
public class FetchBtyLocInfoController {
	@Autowired
    WebService webService;
	
	 private static final Logger logger = LoggerFactory.getLogger(FetchBtyLocInfoController.class);
	 
	 @RequestMapping(value = "/location", method = RequestMethod.POST)
	    public SamResponse fetchLocInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        FetchLocInfoReq req = JSON.parseObject(jsonReq, FetchLocInfoReq.class);
	        try {
	    	
	        	List<BatteryLocInfo> batteryInfos=webService.fetchBtyLocInfo(req.getImei(), req.getCount(), req.getFlag());

	            FetchLocInfoResp  respData = new FetchLocInfoResp();
	            respData.setBatteryInfos(batteryInfos);
	       
	            return ResponseUtils.getNormalResp(respData);
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("fetchBtyLocationInfo exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	    }
	 
}
