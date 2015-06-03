package com.sam.yh.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.enums.BatteryStatus;
import com.sam.yh.model.Battery;
import com.sam.yh.req.bean.vo.SubmitBatteryReqVo;
import com.sam.yh.service.BatteryService;

public class BatteryServiceImpl implements BatteryService {

	@Resource
	private BatteryMapper batteryMapper;

	@Transactional
	@Override
	public Battery submitBattery(SubmitBatteryReqVo submitBatteryReqVo) {
		// TODO Auto-generated method stub
		Battery battery = new Battery();
        // battery.setSn(submitBatteryReqVo.getBatterySN());
        // battery.setStatus(BatteryStatus.NORMAL.getStatus());
        // battery.setBtyType(BooleanUtils.toBoolean(submitBatteryReqVo
        // .getBatteryType()));
        // battery.setImei(submitBatteryReqVo.getImei());
        // battery.setSimNo(submitBatteryReqVo.getSimNo());
        // // battery.setResellerId(resellerId);
        // battery.setSaleStatus(true);
        // battery.setCreateDate(new Date());
        // battery.setSaleDate(new Date());

		batteryMapper.insert(battery);

		return battery;
	}

}
