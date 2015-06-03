package com.sam.yh.dao;

import com.sam.yh.model.BatteryInfo;

public interface BatteryInfoMapper {
    int deleteByPrimaryKey(Integer batteryId);

    int insert(BatteryInfo record);

    int insertSelective(BatteryInfo record);

    BatteryInfo selectByPrimaryKey(Integer batteryId);

    int updateByPrimaryKeySelective(BatteryInfo record);

    int updateByPrimaryKey(BatteryInfo record);
}