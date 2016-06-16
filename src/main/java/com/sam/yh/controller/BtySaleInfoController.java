package com.sam.yh.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.sam.yh.req.bean.BtySaleInfoReq;
import com.sam.yh.resp.bean.BtySaleInfo;
import com.sam.yh.resp.bean.BtySaleInfoResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserBatteryService;


@RestController
@RequestMapping("/bty")

public class BtySaleInfoController {
	@Autowired
    UserBatteryService userBatteryService;
	
	@Resource
	private String adminPhones;

    private static final Logger logger = LoggerFactory.getLogger(BtySaleInfoController.class);

    @RequestMapping(value = "/saleinfo", method = RequestMethod.POST)
    public SamResponse signin(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);
        
        BtySaleInfoReq req = JSON.parseObject(jsonReq, BtySaleInfoReq.class);
        try {
        	validateArgs(req);
        	
        	DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        	Date date=format1.parse(req.getDate());
           
         	List<BtySaleInfo> btySaleInfos= userBatteryService.fetchBtySaleInfo(date);
         	
         	BtySaleInfoResp respData=new BtySaleInfoResp();
         	
         	respData.setBtySaleInfos(btySaleInfos);

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
        	if(e instanceof CrudException){
        		 logger.error("batterySaleinfo exception, " +  e);
        		return ResponseUtils.getServiceErrorResp(e.getMessage());
        	}else{
        		return ResponseUtils.getSysErrorResp();
        	}
           
        }
    }
    
    private void validateArgs(BtySaleInfoReq btySaleInfoReq) throws IllegalParamsException {
        if (!isAdminPhone(btySaleInfoReq.getUserPhone())) {
            throw new IllegalParamsException("你无权查看销售信息！");
        }
        if(!isValidDate(btySaleInfoReq.getDate())){
        	throw new IllegalParamsException("日期格式不正确");
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
    
    private boolean isValidDate(String str) {
    	      boolean convertSuccess=true;
    	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	        try {
    	          format.setLenient(false);
    	           format.parse(str);
    	        } catch (ParseException e) {
    	            convertSuccess=false;
    	        } 
    	       return convertSuccess;
    	 }
    
}

