package com.sam.yh.dao;

import com.sam.yh.model.BatteryInfo;

public interface BatteryInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatteryInfo record);

    int insertSelective(BatteryInfo record);

    BatteryInfo selectByPrimaryKey(Integer id);

    BatteryInfo selectByBtyId(Integer btyId);

}