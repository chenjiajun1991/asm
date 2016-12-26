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
import com.sam.yh.req.bean.web.DeleteBtyReq;
import com.sam.yh.req.bean.web.RemoveBtyReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.RemoveBtyResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/bty")
public class RemoveBtyController {
	@Autowired
    WebService webService;
	 private static final Logger logger = LoggerFactory.getLogger(RemoveBtyController.class);
	 
	 @RequestMapping(value = "/remove", method = RequestMethod.POST)
	    public SamResponse RemoveBattery(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        RemoveBtyReq req = JSON.parseObject(jsonReq, RemoveBtyReq.class);
	        try {
	    	
	        	webService.removeBattery(req.getImei(), req.getReason());

	            RemoveBtyResp  respData = new RemoveBtyResp();
	            respData.setConsequence("ok");
	       
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
	 
	 
	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    public SamResponse DeleteBattery(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        DeleteBtyReq req = JSON.parseObject(jsonReq, DeleteBtyReq.class);
	        try {
	    	
	        	webService.deleteBattery(req.getAdmin());

	            RemoveBtyResp  respData = new RemoveBtyResp();
	            respData.setConsequence("ok");
	       
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
