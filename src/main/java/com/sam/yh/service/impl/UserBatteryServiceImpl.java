package com.sam.yh.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.model.UserBattery;
import com.sam.yh.service.UserBatteryService;

@Service
public class UserBatteryServiceImpl implements UserBatteryService {

    @Resource
    UserBatteryMapper UserBatteryMapper;

    @Override
    public List<UserBattery> fetchUserBattery(int userId) {
        return UserBatteryMapper.selectByUserId(userId);
    }

}
