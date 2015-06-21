package com.sam.yh.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtysException;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.PubBattery;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserFollow;
import com.sam.yh.service.UserBatteryService;

@Service
public class UserBatteryServiceImpl implements UserBatteryService {

    @Resource
    UserBatteryMapper userBatteryMapper;

    @Resource
    UserFollowMapper userFollowMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public List<UserBattery> fetchUserBattery(int userId) {
        return userBatteryMapper.selectByUserId(userId);
    }

    @Override
    public List<UserFollow> fetchUserFollowBty(int userId) {
        return userFollowMapper.selectByUserId(userId);
    }

    @Override
    public UserBattery fetchUserByBtyId(int batteryId) {
        return userBatteryMapper.selectByBtyId(batteryId);
    }

    @Override
    public List<PubBattery> fetchMyBtys(String mobilePhone) throws CrudException {
        User user = userMapper.selectByPhone(mobilePhone);
        if (user == null) {
            throw new FetchBtysException("用户不存在");
        }

        return userBatteryMapper.selectBtysByUserId(user.getUserId());

    }

    @Override
    public List<PubBattery> fetchfriendBtys(String mobilePhone) throws CrudException {
        User user = userMapper.selectByPhone(mobilePhone);
        if (user == null) {
            throw new FetchBtysException("用户不存在");
        }

        return userFollowMapper.selectBtysByUserId(user.getUserId());
    }

}
