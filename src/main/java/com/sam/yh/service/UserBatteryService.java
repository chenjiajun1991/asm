package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserFollow;

public interface UserBatteryService {

    @Transactional
    public List<UserBattery> fetchUserBattery(int userId);

    @Transactional
    public List<UserFollow> fetchUserFollowBty(int userId);

    @Transactional
    public UserBattery fetchUserByBtyId(int batteryId);
}
