package com.sam.yh.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.PubBattery;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryInfo;
import com.sam.yh.model.UserFollow;
import com.sam.yh.resp.bean.BtyFollower;
import com.sam.yh.resp.bean.BtySaleInfo;


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

    @Transactional
    public List<BtyFollower> fetchBtyFollowers(String userName, String btyPubSn) throws CrudException;
    
    //查询当天销售的电池信息
    @Transactional
    public List<BtySaleInfo> fetchBtySaleInfo(Date date)throws CrudException;
    
    
}
