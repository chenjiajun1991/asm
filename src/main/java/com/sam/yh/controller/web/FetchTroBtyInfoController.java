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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.web.TroubleBtyInfo;
import com.sam.yh.req.bean.web.FetchAllTroBtyInfoReq;
import com.sam.yh.req.bean.web.FetchTroBtyInfoReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.FetchAllTroBtyInfoResp;
import com.sam.yh.resp.bean.web.FetchTroBtyInfoResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/bty")
public class FetchTroBtyInfoController {
	
	@Autowired
    WebService webService;
	
	@Resource
	private String adminPhones;
	
	 private static final Logger logger = LoggerFactory.getLogger(FetchTroBtyInfoController.class);
	 
	 @RequestMapping(value = "/trobtyinfos", method = RequestMethod.POST)
	    public SamResponse FetchAllTroubleBtyInfos(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        FetchAllTroBtyInfoReq req = JSON.parseObject(jsonReq, FetchAllTroBtyInfoReq.class);
	        try {
	        	validateArgs(req);
	        	
	            List<TroubleBtyInfo> troubleBtyInfos = webService.fetchAllTroBty();
	 
	         	FetchAllTroBtyInfoResp respData =new FetchAllTroBtyInfoResp();
	           respData.setTroubleBtyInfos(troubleBtyInfos);
	       
	            return ResponseUtils.getNormalResp(respData);
	        } catch (IllegalParamsException e) {
	            return ResponseUtils.getParamsErrorResp(e.getMessage());
	        } catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("fetchAllTroinfos exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	    }
	 
	 @RequestMapping(value = "/trobtyprams", method = RequestMethod.POST)
	    public SamResponse FetchTroBtyInfoByImeiOrPhone(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        FetchTroBtyInfoReq req = JSON.parseObject(jsonReq, FetchTroBtyInfoReq.class);
	        try {
	            List<TroubleBtyInfo> troubleBtyInfos = webService.fetchTroBtyByImeiOrPhone(req.getBtyParms());
	 
	         	FetchTroBtyInfoResp respData =new FetchTroBtyInfoResp();
	            respData.setTroubleBtyInfos(troubleBtyInfos);
	       
	            return ResponseUtils.getNormalResp(respData);
	        }  catch (Exception e) {
	        	if(e instanceof CrudException){
	        		 logger.error("fetchTroinfo by prams exception, " +  e);
	        		return ResponseUtils.getServiceErrorResp(e.getMessage());
	        	}else{
	        		return ResponseUtils.getSysErrorResp();
	        	}
	           
	        }
	    }
	 
	 
	 
	 private void validateArgs(FetchAllTroBtyInfoReq fetchAllTroBtyInfoReq) throws IllegalParamsException {
	        if (!isAdminPhone(fetchAllTroBtyInfoReq.getAdmin())) {
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
