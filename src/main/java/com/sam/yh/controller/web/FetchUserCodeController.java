package com.sam.yh.controller.web;

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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.web.CodeInfoModel;
import com.sam.yh.req.bean.web.FetchCodeInfoReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.FetchCodeInfoResp;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/user")
public class FetchUserCodeController {
	@Autowired
    WebService webService;
	 private static final Logger logger = LoggerFactory.getLogger(FetchUserCodeController.class);
	 @RequestMapping(value = "/codeinfos", method = RequestMethod.POST)
	    public SamResponse fetchUserCodeInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

	        logger.info("Request json String:" + jsonReq);
	        FetchCodeInfoReq req = JSON.parseObject(jsonReq, FetchCodeInfoReq.class);
	        try {
	    	
	        	List<CodeInfoModel> codeInfoModels=webService.fetchUserCode(req.getMobilePhone());

	            FetchCodeInfoResp  respData = new FetchCodeInfoResp();
	            respData.setCodeInfoModels(codeInfoModels);
	       
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
