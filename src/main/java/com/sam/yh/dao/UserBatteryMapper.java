package com.sam.yh.dao;

import java.util.List;
import java.util.Map;

import com.sam.yh.model.PubBattery;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryInfo;
import com.sam.yh.model.UserBatteryKey;
import com.sam.yh.model.web.BtySaleInfoModel;

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
    
    List<UserBatteryInfo> selectByBuyDate(Map<String,Object> map);
    
    //查询所有电池的销售信息
    List<BtySaleInfoModel> selectAllBtySaleInfo();
    
    BtySaleInfoModel selectBtySaleInfoByImei(String imei);
    
    List<BtySaleInfoModel> selectBtySaleInfoByPhone(String mobilePhone);
    
    int deleteByBtyId(int batteryId);
    
    int updateByBtyId(UserBattery userBattery);
    
}