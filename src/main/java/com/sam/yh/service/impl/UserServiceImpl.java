package com.sam.yh.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserCode;
import com.sam.yh.service.UserBatteryService;
import com.sam.yh.service.UserCodeService;
import com.sam.yh.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserCodeService userCodeService;

    @Resource
    private UserBatteryService userBatteryService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    @Override
    public int signup(String userName, String authCode, String hassPwd, String deviceInfo) throws CrudException {

        boolean auth = userCodeService.verifyAuthCode(userName, UserCodeType.SIGNUP_CODE.getType(), authCode);
        if (!auth) {
            throw new UserSignupException("短信验证码错误");
        }

        UserCode userCode = userCodeService.fetchByUserName(userName, UserCodeType.USER_SALT.getType());
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        User user = new User();
        user.setUuid(StringUtils.replace(uuid, "-", ""));
        user.setUserName(userName);
        user.setSalt(userCode.getDynamicCode());
        user.setPassword(hassPwd);
        user.setMobilePhone(userName);
        user.setLockStatus(false);
        user.setDeviceInfo(deviceInfo);
        user.setCreateDate(now);
        user.setLoginDate(now);

        return userMapper.insert(user);
    }

    @Override
    public int signin(String userName, String randCode, String hassPwd, String deviceInfo) throws CrudException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int resetPwd(String userName, String authCode, String hassPwd, String deviceInfo) throws CrudException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updatePwd(String userName, String authCode, String oldHassPwd, String newHashPwd, String deviceInfo) throws CrudException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<BatteryInfo> fetchBtyInfo(String userName) {
        User user = userMapper.selectByUserName(userName);
        List<UserBattery> btys = userBatteryService.fetchUserBattery(user.getUserId());
        List<BatteryInfo> btyInfo = new ArrayList<BatteryInfo>();
        for (UserBattery userBattery : btys) {
            btyInfo.add(batteryInfoMapper.selectByBtyId(userBattery.getBatteryId()));
        }
        return btyInfo;
    }
}
