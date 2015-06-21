package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.PubBattery;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserFollow;

public interface UserBatteryService {

    @Transactional
    public List<UserBattery> fetchUserBattery(int userId);

    @Transactional
    public List<UserFollow> fetchUserFollowBty(int userId);

    @Transactional
    public UserBattery fetchUserByBtyId(int batteryId);

    @Transactional
    public List<PubBattery> fetchMyBtys(String mobilePhone) throws CrudException;

    @Transactional
    public List<PubBattery> fetchfriendBtys(String mobilePhone) throws CrudException;
}
