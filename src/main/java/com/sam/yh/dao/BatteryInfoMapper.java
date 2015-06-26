package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.BatteryInfo;
import com.sam.yh.resp.bean.ResellerBtyInfo;

public interface BatteryInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BatteryInfo record);

    int insertSelective(BatteryInfo record);

    BatteryInfo selectByPrimaryKey(Integer id);

    BatteryInfo selectByBtyId(Integer btyId);

    List<ResellerBtyInfo> selectByReseller(Integer resellerId);

}