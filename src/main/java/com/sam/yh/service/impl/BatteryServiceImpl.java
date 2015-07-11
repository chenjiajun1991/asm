package com.sam.yh.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.common.TempUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtyInfoException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.req.bean.BatteryInfoReq;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.UserBatteryService;
import com.sam.yh.service.UserCodeService;

@Service
public class BatteryServiceImpl implements BatteryService {

    @Resource
    private BatteryMapper batteryMapper;

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    @Resource
    private UserBatteryService userBatteryService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCodeService userCodeService;

    @Override
    public Battery uploadBatteryInfo(BatteryInfoReq batteryInfoReqVo) throws CrudException {
        Battery battery = fetchBtyByIMEI(batteryInfoReqVo.getImei());
        if (battery == null) {
            return null;
        }
        if (battery.getImsi() == null || battery.getGsmSimNo() == null) {
            if (batteryInfoReqVo.getImsi() != null) {
                battery.setImsi(batteryInfoReqVo.getImsi());
            }
            if (batteryInfoReqVo.getPhonenumber() != null) {
                battery.setGsmSimNo(batteryInfoReqVo.getPhonenumber());
            }
            batteryMapper.updateByPrimaryKeySelective(battery);
        }
        BatteryInfo info = new BatteryInfo();
        info.setBatteryId(battery.getId());
        info.setLongitude(batteryInfoReqVo.getLongitude());
        info.setLatitude(batteryInfoReqVo.getLatitude());
        info.setTemperature(convertAdcToTemp(batteryInfoReqVo.getTemperature()));
        info.setVoltage(convertAdcToVo(batteryInfoReqVo.getVoltage()));
        // TODO
        // info.setSampleDate(batteryInfoReqVo.getSampleDate());
        info.setSampleDate(new Date());
        boolean status = getBatteryStatus(batteryInfoReqVo);
        info.setStatus(status);
        info.setReceiveDate(new Date());

        batteryInfoMapper.insert(info);

        if (!status) {
            sendWarningMsg(battery);
        }

        return battery;
    }

    private String convertAdcToTemp(String adc) {
        return TempUtils.isWarning(adc) ? adc : TempUtils.getTemp(adc);
    }

    private String convertAdcToVo(String adc) {
        float tem = Float.valueOf(adc);
        float vol = (float) ((int) ((tem / 10.73) * 10)) / 10;
        return String.valueOf(vol);
    }

    private boolean getBatteryStatus(BatteryInfoReq batteryInfoReqVo) {
        boolean status = true;
        String adcTmp = batteryInfoReqVo.getTemperature();
        if (TempUtils.isWarning(adcTmp)) {
            status = false;
        }

        float adcVol = Float.valueOf(batteryInfoReqVo.getVoltage());
        if (adcVol < 10 || adcVol > 90) {
            status = false;
        }

        return status;
    }

    private void sendWarningMsg(Battery battery) throws CrudException {

        UserBattery userBattery = userBatteryService.fetchUserByBtyId(battery.getId());
        User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
        userCodeService.sendWarningMsg(user.getMobilePhone(), battery.getImei());
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

    @Override
    public BatteryInfo fetchBtyInfo(String btySimNo) throws CrudException {
        Battery battery = batteryMapper.selectBySimNo(btySimNo);
        if (battery == null) {
            throw new FetchBtyInfoException("电池不存在");
        }

        BatteryInfo info = batteryInfoMapper.selectByBtyId(battery.getId());

        if (info == null) {
            throw new FetchBtyInfoException("未接收到此电池发送的信息");
        }

        return info;
    }

}
