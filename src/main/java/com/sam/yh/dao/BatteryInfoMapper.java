package com.sam.yh.dao;

import com.sam.yh.model.BatteryInfo;

public interface BatteryInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatteryInfo record);

    int insertSelective(BatteryInfo record);

    BatteryInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BatteryInfo record);

    int updateByPrimaryKey(BatteryInfo record);
}