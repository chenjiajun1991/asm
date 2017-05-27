package com.sam.yh.controller.web;


import java.util.ArrayList;
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
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
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
	
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private BatteryMapper batteryMapper;
    
    @Resource
    private UserBatteryMapper userBatteryMapper;

    private static final Logger logger = LoggerFactory.getLogger(BtySaleInfoController.class);

    @RequestMapping(value = "/detelinfo", method = RequestMethod.POST)
    public SamResponse signin(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);
        FetchAllSaleInfoReq req = JSON.parseObject(jsonReq, FetchAllSaleInfoReq.class);
        try {
        	validateArgs(req);
        	
        	List<BtySaleInfoModel> btySaleInfos = null;
        	
        	if(isAdminPhone(req.getAccount())){
        		
        		 btySaleInfos = webService.fetchAllSaleInfo();
        		
        	}else if(isResellerPhone(req.getAccount())){
        		
        		btySaleInfos = webService.fetchResellerSaleInfo(req.getAccount());
        		
        	}
 
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
        	
        	validateImei(req.getImei(), req.getAccount());
    	
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
        	
        	
        	
        	validatePhone(req.getMobilePhone(),req.getAccount());
        	
    	
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
    
     
	private void validatePhone(String userPhone , String account) throws CrudException {
           if(!isAdminPhone(account)){
			
			User user = userMapper.selectByPhone(userPhone);
			if(user == null){
				throw new CrudException("用户不存在！");
			}
			
			User reseller = userMapper.selectByPhone(account);
			
			List<UserBattery> userBatterys = userBatteryMapper.selectByUserId(user.getUserId());
			
			List<Integer> btyResellerIds = new ArrayList<Integer>();
			
			for(UserBattery userBattery : userBatterys){
				
				Battery battery = batteryMapper.selectByPrimaryKey(userBattery.getBatteryId());
				
				btyResellerIds.add(battery.getResellerId());
			}
			
			if(!btyResellerIds.contains(reseller.getUserId())){
				throw new CrudException("该用户尚未和你购买过电池！");
			}
					
		}
    }
	
	
	
	private void validateImei(String imei , String account) throws CrudException {
        if(!isAdminPhone(account)){
			
			Battery battery = batteryMapper.selectByIMEI(imei);
			if(battery == null){
				throw new CrudException("IMEI号不存在！");
			}
			
			User reseller = userMapper.selectByPhone(account);
			
			
			if(!battery.getResellerId().equals(reseller.getUserId())){
				throw new CrudException("该电池不是由你出售的，你暂时无权查看！");
			}
					
		}
 }
    
    
    

	private void validateArgs(FetchAllSaleInfoReq fetchAllSaleInfoReq) throws IllegalParamsException {
        if (!isAdminPhone(fetchAllSaleInfoReq.getAccount()) && !isResellerPhone(fetchAllSaleInfoReq.getAccount())) {
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
    
    private boolean isResellerPhone(String userPhone) throws IllegalParamsException{
    	
	       User user = userMapper.selectByPhone(userPhone);
	       
	       if(user == null){
	    	   throw new IllegalParamsException("用户不存在！");
	       }
	       
	       if(user.getUserType().equals("1")){
	    	   return true;
	       }
	       
	       return false;
	    }
   
}
