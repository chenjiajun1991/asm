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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.web.BreakBtyInfo;
import com.sam.yh.req.bean.web.FetchAllBreakBtyInfoReq;
import com.sam.yh.req.bean.web.RemindOffLineReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.FetchAllBreakBtyResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/bty")
public class BreakBtyInfoController {
	
	@Autowired
    WebService webService;
	
	@Resource
	private String adminPhones;
	
	 private static final Logger logger = LoggerFactory.getLogger(BreakBtyInfoController.class);
	 
	 @RequestMapping(value = "/break/btyinfos", method = RequestMethod.POST)
	    public SamResponse FetchAllBreakBtyInfos(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        FetchAllBreakBtyInfoReq req = JSON.parseObject(jsonReq, FetchAllBreakBtyInfoReq.class);
	        try {
	        	
	        	FetchAllBreakBtyResp respData = new FetchAllBreakBtyResp();
	        	
	        	if(isAdminPhone(req.getUserPhone())){
	        		List<BreakBtyInfo> breakBtyInfos = webService.fetchAllBreakBtyInfo();
	        		respData.setBreakBtyInfo(breakBtyInfos);
	        		
	        	}else{
	        		List<BreakBtyInfo> breakBtyInfos = webService.fetchBreakBtyInfoByReseller(req.getUserPhone());
	        		respData.setBreakBtyInfo(breakBtyInfos);
	        	}
	        	       
	            return ResponseUtils.getNormalResp(respData);
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("fetchAllBreakBtyinfos exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	    }
	 
	 
	 @RequestMapping(value = "/remind/offline", method = RequestMethod.POST)
	    public SamResponse RemindOffLineBty(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        RemindOffLineReq req = JSON.parseObject(jsonReq, RemindOffLineReq.class);
	        try {
	        	
	        	if(isAdminPhone(req.getUserPhone())){
	        		
	        		 webService.remindOffLineBty();
	        		
	        		 return ResponseUtils.getNormalResp("ok");
	        		 
	        	}else{
	        		 return ResponseUtils.getNormalResp("你无权操作");
	        	}
	        	       
	           
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("RemindOffLineBty exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
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
