package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.TroubleBattery;

public interface TroubleBatteryMapper {
	
	List<TroubleBattery> selectAllTroubleBty();
	
	TroubleBattery selectBtyById(int batteryId);
	
	TroubleBattery selectBtyByIMEI(String imei);
	
	 int deleteBtyById(int batteryId);
	 
	 int insert(TroubleBattery troubleBattery);
	 
	 int updateById(TroubleBattery troubleBattery);

}
