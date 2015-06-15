package com.sam.yh.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.SubmitBtySpecException;
import com.sam.yh.dao.ResellerMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.BatteryStatus;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.model.Battery;
import com.sam.yh.model.Reseller;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.req.bean.SubmitBtySpecReq;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.ResellerService;
import com.sam.yh.service.UserCodeService;
import com.sam.yh.service.UserService;

@Service
public class ResellerServiceImpl implements ResellerService {

    @Resource
    UserService userService;

    @Resource
    UserCodeService userCodeService;

    @Resource
    BatteryService batteryService;

    @Resource
    UserMapper userMapper;

    @Resource
    UserFollowMapper userFollowMapper;

    @Resource
    ResellerMapper resellerMapper;

    @Override
    public void submitBtySpec(SubmitBtySpecReq submitBtySpecReq) throws CrudException {
        //
        User user = userService.fetchUserByPhone(submitBtySpecReq.getUserPhone());
        if (user == null) {
            user = addLockedUserBySys(submitBtySpecReq.getUserName(), submitBtySpecReq.getUserPhone());
            userCodeService.sendAndSaveSmsCode(submitBtySpecReq.getUserPhone(), UserCodeType.SIGNUP_CODE.getType());
        }

        //
        User resellerUser = userService.fetchUserByPhone(submitBtySpecReq.getResellerPhone());
        Reseller reseller = resellerMapper.selectByPrimaryKey(resellerUser.getUserId());
        if (resellerUser == null || reseller == null) {
            throw new SubmitBtySpecException("经销商不存在，请联系客服。");
        }

        //
        boolean isCloudBty = true;
        Battery battery = addBattery(submitBtySpecReq.getBtySN(), submitBtySpecReq.getBtyImei(), submitBtySpecReq.getBtySimNo(), isCloudBty,
                resellerUser.getUserId());

        //
        UserBattery userBattery = new UserBattery();
        userBattery.setBatteryId(battery.getId());
        userBattery.setUserId(user.getUserId());
        userBattery.setBuyDate(new Date());

    }

    private User addLockedUserBySys(String userName, String mobilePhone) {
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        User user = new User();
        user.setUuid(StringUtils.replace(uuid, "-", ""));
        user.setUserName(userName);
        user.setSalt(RandomCodeUtils.genSalt());
        user.setPassword(RandomCodeUtils.genSalt());
        user.setMobilePhone(mobilePhone);
        user.setLockStatus(true);
        user.setCreateDate(now);

        userMapper.insertSelective(user);

        return user;
    }

    private Battery addBattery(String btySn, String imei, String simNo, boolean isCloudBty, int resellerId) {
        Date now = new Date();
        Battery battery = new Battery();
        battery.setSn(btySn);
        battery.setImei(imei);
        battery.setSimNo(simNo);
        battery.setBtyType(isCloudBty);
        battery.setStatus(BatteryStatus.NORMAL.getStatus());
        battery.setResellerId(resellerId);
        battery.setSaleStatus(true);
        battery.setCreateDate(now);
        battery.setSaleDate(now);

        return batteryService.addBattery(battery);
    }
}