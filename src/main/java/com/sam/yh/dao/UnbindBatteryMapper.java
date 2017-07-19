package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.UnbindBattery;

public interface UnbindBatteryMapper {
	
	List<UnbindBattery> selectAllUnbindBty();
	
	UnbindBattery selectByPrimaryKey(int id);
	
	UnbindBattery selectByIMEI(String imei);
	
	int deleteByPrimaryKey(int id);
	
	int insertSelective(UnbindBattery unbindBattery);
	
}
