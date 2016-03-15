package com.sam.yh.dao;

import com.sam.yh.model.BatteryInfoNst;

public interface BatteryInfoNstMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BatteryInfoNst record);

    int insertSelective(BatteryInfoNst record);

    BatteryInfoNst selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BatteryInfoNst record);

    int updateByPrimaryKey(BatteryInfoNst record);

    BatteryInfoNst selectByBtyId(Integer batteryId);
}