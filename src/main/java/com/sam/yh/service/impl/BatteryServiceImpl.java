package com.sam.yh.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.req.bean.BatteryInfoReq;
import com.sam.yh.service.BatteryService;

@Service
public class BatteryServiceImpl implements BatteryService {

    @Resource
    private BatteryMapper batteryMapper;

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    @Override
    public Battery uploadBatteryInfo(BatteryInfoReq batteryInfoReqVo) {
        Battery battery = fetchBtyByIMEI(batteryInfoReqVo.getImei());
        if (battery == null) {
            return null;
        }
        BatteryInfo info = new BatteryInfo();
        info.setBatteryId(battery.getId());
        info.setLongitude(batteryInfoReqVo.getLongitude());
        info.setLatitude(batteryInfoReqVo.getLatitude());
        info.setTemperature(batteryInfoReqVo.getTemperature());
        info.setVoltage(batteryInfoReqVo.getVoltage());
        // TODO
        // info.setSampleDate(batteryInfoReqVo.getSampleDate());
        info.setSampleDate(new Date());
        info.setStatus(getBatteryStatus(batteryInfoReqVo));
        info.setReceiveDate(new Date());

        batteryInfoMapper.insert(info);

        return battery;
    }

    private boolean getBatteryStatus(BatteryInfoReq batteryInfoReqVo) {
        // TODO
        return true;
    }

    @Override
    public Battery addBattery(Battery battery) {
        batteryMapper.insertSelective(battery);
        return battery;
    }

    @Override
    public Battery fetchBtyByIMEI(String imei) {
        return batteryMapper.selectByIMEI(imei);
    }

    @Override
    public Battery fetchBtyByPubSn(String pubSn) {
        return batteryMapper.selectByPubSn(pubSn);
    }

    @Override
    public Battery fetchBtyBySimNo(String simNo) {
        return batteryMapper.selectBySimNo(simNo);
    }

    @Override
    public Battery fetchBtyBySN(String btySn) {
        return batteryMapper.selectBySn(btySn);
    }

    @Override
    public int countSoldBtys(int resellerId) {
        return batteryMapper.countByReseller(resellerId);
    }

    @Override
    public int countCityBtys(int cityId) {
        return batteryMapper.countByCity(cityId);
    }

}
