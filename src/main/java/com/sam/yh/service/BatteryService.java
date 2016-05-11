package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfoNst;
import com.sam.yh.req.bean.BatteryInfoReq;

public interface BatteryService {

    @Transactional
    public Battery uploadBatteryInfo(BatteryInfoReq batteryInfoReqVo) throws CrudException;

    @Transactional
    public Battery addBattery(Battery battery);

    @Transactional
    public Battery fetchBtyByIMEI(String imei);

    public Battery fetchBtyById(int id);

    @Transactional
    public Battery fetchBtyByPubSn(String pubSn);

    @Transactional
    public Battery fetchBtyBySimNo(String simNo);

    @Transactional
    public Battery fetchBtyBySN(String btySn);

    @Transactional
    public int countSoldBtys(int resellerId);

    @Transactional
    public int countCityBtys(int cityId);

    @Transactional
    public BatteryInfoNst fetchBtyInfo(String btySimNo) throws CrudException;
}
