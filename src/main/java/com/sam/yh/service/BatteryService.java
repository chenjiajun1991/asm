package com.sam.yh.service;

import com.sam.yh.model.Battery;
import com.sam.yh.req.bean.vo.SubmitBatteryReqVo;

public interface BatteryService {

	public Battery submitBattery(SubmitBatteryReqVo submitBatteryReqVo);
}
