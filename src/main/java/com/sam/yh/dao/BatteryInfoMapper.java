package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.ResellerBtyInfo;
import com.sam.yh.model.web.BtyCountInfo;


public interface BatteryInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BatteryInfo record);

    int insertSelective(BatteryInfo record);

    BatteryInfo selectByPrimaryKey(Long id);

    BatteryInfo selectByBtyId(Integer btyId);

    List<ResellerBtyInfo> selectByReseller(Integer resellerId);
    
    List<BatteryInfo> selectBtyByIdAndCountDesc(BtyCountInfo btyCountInfo);
    
    List<BatteryInfo> selectBtyByIdAndCountAsc(BtyCountInfo btyCountInfo);
    
}