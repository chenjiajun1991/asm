package com.sam.yh.dao;

import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryKey;

public interface UserBatteryMapper {
    int deleteByPrimaryKey(UserBatteryKey key);

    int insert(UserBattery record);

    int insertSelective(UserBattery record);

    UserBattery selectByPrimaryKey(UserBatteryKey key);

    int updateByPrimaryKeySelective(UserBattery record);

    int updateByPrimaryKey(UserBattery record);
}