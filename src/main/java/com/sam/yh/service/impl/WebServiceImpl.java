package com.sam.yh.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.User;
import com.sam.yh.service.UserService;
import com.sam.yh.service.WebService;

@Service
public class WebServiceImpl implements WebService{
	
	@Resource
	UserService userService;
	
    @Resource
    private String commonPwd;
    
    @Resource
    private UserMapper userMapper;

	@Override
	public User adminLogin(String account, String hassPwd)
			throws CrudException {

        User user = userService.fetchUserByPhone(account);
        if (user == null || user.getLockStatus()) {
            throw new UserSignupException("用户不存在");
        }

        if ((!StringUtils.equals(user.getPassword(), PwdUtils.genMd5Pwd(account, user.getSalt(), hassPwd)))&&(!hassPwd.equals(commonPwd))) {
         throw new UserSignupException("密码错误");
         }
        
        if (!user.getUserType().equals(userService.getUserType(account))) {
            user.setUserType(userService.getUserType(account));
        }
        user.setLoginDate(new Date());
        user.setDeviceInfo("web");
        userMapper.updateByPrimaryKeySelective(user);
        
        return user;
	}
	
}
