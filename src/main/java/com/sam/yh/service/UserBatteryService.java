package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.model.UserBattery;

public interface UserBatteryService {

    @Transactional
    public List<UserBattery> fetchUserBattery(int userId);

}