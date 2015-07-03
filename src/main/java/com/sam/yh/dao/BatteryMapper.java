package com.sam.yh.dao;

import com.sam.yh.model.Battery;

public interface BatteryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Battery record);

    int insertSelective(Battery record);

    Battery selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Battery record);

    int updateByPrimaryKey(Battery record);

    Battery selectByIMEI(String imei);

    Battery selectByPubSn(String pubSn);

    Battery selectBySimNo(String simNo);

    Battery selectBySn(String btySn);

    int countByReseller(Integer resellerId);

    int countByCity(Integer cityId);
}