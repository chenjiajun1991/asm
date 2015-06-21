package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.PubBattery;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryKey;

public interface UserBatteryMapper {
    int deleteByPrimaryKey(UserBatteryKey key);

    int insert(UserBattery record);

    int insertSelective(UserBattery record);

    UserBattery selectByPrimaryKey(UserBatteryKey key);

    int updateByPrimaryKeySelective(UserBattery record);

    int updateByPrimaryKey(UserBattery record);

    List<UserBattery> selectByUserId(Integer userId);

    UserBattery selectByBtyId(Integer batteryId);
    
    List<PubBattery> selectBtysByUserId(Integer userId);
}