package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.TroubleUserBattery;

public interface TroubleUserBatteryMapper {
	
	TroubleUserBattery selectByBtyId(int batteryId);
	
	List<TroubleUserBattery> selectByUserId(int userId);
	
	int deleteByBtyId(int batteryId);
	
	int insert(TroubleUserBattery troubleUserBattery);
	
	int updateByBtyId(TroubleUserBattery troubleUserBattery);

}
