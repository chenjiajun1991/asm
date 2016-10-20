package com.sam.yh.controller.web;

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
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.model.User;
import com.sam.yh.req.bean.UserSigninReq;
import com.sam.yh.req.bean.web.AdminLoginReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.web.AdminLoginResp;
import com.sam.yh.service.UserService;
import com.sam.yh.service.WebService;

@RestController
@RequestMapping("/admin")
public class AdminLoginController {
	    @Autowired
	    WebService webService;

	    @Resource
	    private String adminPhones;

	    private static final Logger logger = LoggerFactory.getLogger(AdminLoginController.class);

	    @RequestMapping(value = "/login", method = RequestMethod.POST)
	    public SamResponse signin(HttpServletRequest httpServletRequest,  @RequestParam("jsonReq")String jsonReq) {
	    	
	        logger.info("Request json String:" + jsonReq);

	        AdminLoginReq req = JSON.parseObject(jsonReq, AdminLoginReq.class);

	        try {
	        	validateSigninArgs(req);
	        	User user = webService.adminLogin(req.getAccount(), req.getPassword());
	        	
	         
	         AdminLoginResp respData = new AdminLoginResp();
	         respData.setUserUid(user.getUuid());
	         return ResponseUtils.getNormalResp(respData);
	         
	        }catch (IllegalParamsException e) {
	            return ResponseUtils.getParamsErrorResp(e.getMessage());
	        }catch (CrudException e) {
	            logger.error("login exception, " + req.getAccount(), e);
	            if (e instanceof UserSignupException) {
	                return ResponseUtils.getServiceErrorResp(e.getMessage());
	            } else {
	                return ResponseUtils.getSysErrorResp();
	            }
	        }catch (Exception e) {
	            logger.error("admin login exception, " + req.getAccount(), e);
	            return ResponseUtils.getSysErrorResp();
	        }
	    } 
	    
	    private void validateSigninArgs( AdminLoginReq adminLoginReq) throws IllegalParamsException {
	        if (!isAdminPhone(adminLoginReq.getAccount())) {
	            throw new IllegalParamsException("对不起，你无权登录！");
	        }

	        if (!PwdUtils.isValidPwd(adminLoginReq.getPassword())) {
	            throw new IllegalParamsException("密码长度为8-20位字符");
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
