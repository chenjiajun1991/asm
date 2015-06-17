package com.sam.yh.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserFollow;
import com.sam.yh.service.UserBatteryService;

@Service
public class UserBatteryServiceImpl implements UserBatteryService {

    @Resource
    UserBatteryMapper UserBatteryMapper;

    @Resource
    UserFollowMapper userFollowMapper;

    @Override
    public List<UserBattery> fetchUserBattery(int userId) {
        return UserBatteryMapper.selectByUserId(userId);
    }

    @Override
    public List<UserFollow> fetchUserFollowBty(int userId) {
        return userFollowMapper.selectByUserId(userId);
    }

    @Override
    public UserBattery fetchUserByBtyId(int batteryId) {
        return UserBatteryMapper.selectByBtyId(batteryId);
    }

}
