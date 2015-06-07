package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.model.Battery;
import com.sam.yh.req.bean.BatteryInfoReq;

public interface BatteryService {

    @Transactional
    public Battery uploadBatteryInfo(BatteryInfoReq batteryInfoReqVo);
}
