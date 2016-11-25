package com.sam.yh.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.sam.yh.common.DistanceUtils;
import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.TempUtils;
import com.sam.yh.common.baidugps.SendGpsToBaiduServer;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtyInfoException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryInfoNstMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.BatteryStatus;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.BatteryInfoNst;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.req.bean.BatteryInfoReq;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.UserBatteryService;
import com.sam.yh.service.UserCodeService;

@Service
public class BatteryServiceImpl implements BatteryService {

    private static final Logger logger = LoggerFactory.getLogger(BatteryServiceImpl.class);

    @Resource
    private BatteryMapper batteryMapper;

    @Resource
    private BatteryInfoNstMapper batteryInfoNstMapper;

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    @Resource
    private UserBatteryService userBatteryService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCodeService userCodeService;
    
    @Resource
    private Long MoveDis;
    
    @Resource
    private String servicePhone;
    
    @Resource
    private SendGpsToBaiduServer sendGpsToBaiduServer;
    
 
 
    @Override
    public Future<Battery> uploadBatteryInfo(BatteryInfoReq batteryInfoReqVo) throws CrudException {
        if (batteryInfoReqVo == null) {
            return null;
        }
        logger.info("Upload battery info request:" + batteryInfoReqVo);
        Battery battery = fetchBtyByIMEI(batteryInfoReqVo.getImei());
        if (battery == null) {
        	//test
//        	 Date now = new Date();
//             battery = new Battery();
//             battery.setSn(batteryInfoReqVo.getImei());
//             battery.setPubSn(RandomCodeUtils.genBtyPubSn());
//             battery.setImei(batteryInfoReqVo.getImei());
//             battery.setIccid("123456");
//             battery.setSimNo(batteryInfoReqVo.getImei());
//             battery.setBtyType(true);
//             battery.setStatus(BatteryStatus.NORMAL.getStatus());
//             battery.setResellerId(33);
//             battery.setCityId(100);
//             battery.setSaleStatus(true);
//             battery.setCreateDate(now);
//             battery.setSaleDate(now);
//             battery.setBtyQuantity(4);
//
//            addBattery(battery);
        	 	
            return new AsyncResult<Battery>(null);
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
        String voltage = convertAdcToVo(batteryInfoReqVo.getVoltage());
        info.setVoltage(voltage);
        
        // 临时代码，调整电池的节数
        int BtyQuantity = battery.getBtyQuantity() == null ? 4 : battery.getBtyQuantity();
        Date BtySaleDate = battery.getSaleDate();
        Date now  = new Date();
        
        //调整4节组的临界电压值
        if (BtyQuantity == 4 && Double.parseDouble(voltage) > 62d && now.before(DateUtils.addHours(BtySaleDate, 2))) {
            Battery temBattery = new Battery();
            temBattery.setId(battery.getId());
            BtyQuantity = 5;
            temBattery.setBtyQuantity(BtyQuantity);
            batteryMapper.updateByPrimaryKeySelective(temBattery);
        }
        
        
        //当电压过高或过低时发短信提示用户
        if(BtyQuantity==4){
        	if(Double.parseDouble(voltage)>62d){
        		sendVolageWarningMsg(battery, voltage, 1);
        	}else if(Double.parseDouble(voltage)<41.5d){
        		sendVolageWarningMsg(battery, voltage, 0);
        	}
        }else if(BtyQuantity==5){
            if(Double.parseDouble(voltage)>77.5d){
            	sendVolageWarningMsg(battery, voltage, 1);
            	 Date date=new Date();
            	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String dateString = formatter.format(date);
                 logger.info("sendHigeVoltageTime1:" + dateString);
            	
        	}else if(Double.parseDouble(voltage)<52d){
        		sendVolageWarningMsg(battery, voltage, 0);
        	}
        }
        
        
        //增加一个剪断信号线电压突变报警
        if(Double.parseDouble(voltage)<15.5d){
        	sendViolentDestroyClient(battery);
        	sendViolentDestroyService(servicePhone, battery);
        }
        
        
   
        // TODO
        // info.setSampleDate(batteryInfoReqVo.getSampleDate());
        info.setSampleDate(new Date());
        BatteryStatus status = getBatteryStatus(batteryInfoReqVo);
        info.setStatus(status.getStatus());
        info.setReceiveDate(new Date());

        batteryInfoMapper.insert(info);

        updateBatteryInfoNst(info);
        
        
        //若经纬度信息不为零，发送电池信息到百度鹰眼服务器
        if(Double.parseDouble(batteryInfoReqVo.getLatitude())!=0&&Double.parseDouble(batteryInfoReqVo.getLongitude())!=0){
        	
        	String result=sendGpsToBaiduServer.sendGPStoBaiDu(batteryInfoReqVo.getImei(), 
            		Double.parseDouble(batteryInfoReqVo.getLatitude()),Double.parseDouble(batteryInfoReqVo.getLongitude()) ,
            		System.currentTimeMillis()/1000);
        	try {
				String s=new String(result.getBytes(),"utf-8");
				logger.info("sendGpsToBaiduServer:" +s+batteryInfoReqVo.getLatitude()+","+batteryInfoReqVo.getLongitude());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		
        }else{
        	Battery bty=batteryMapper.selectByIMEI(batteryInfoReqVo.getImei());
        	BatteryInfoNst nstInfo = batteryInfoNstMapper.selectByBtyId(bty.getId());
        	
        	String result=sendGpsToBaiduServer.sendGPStoBaiDu(batteryInfoReqVo.getImei(), 
            		Double.parseDouble(nstInfo.getLatitude()),Double.parseDouble(nstInfo.getLongitude()),
            		System.currentTimeMillis()/1000);
        	try {
				String s=new String(result.getBytes(),"utf-8");
				logger.info("sendGpsToBaiduServerNst:" +s+nstInfo.getLatitude()+","+nstInfo.getLongitude());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        

        if (TempUtils.isWarning(batteryInfoReqVo.getTemperature())) {
            sendWarningMsg(battery);
        }

        if (Double.parseDouble(voltage) < (BtyQuantity * 10.5d + 1)) {
//            sendVolageWarningMsg(battery, voltage);
        }

        if (BatteryStatus.LOCKED.getStatus().equals(battery.getStatus())&&Double.parseDouble(batteryInfoReqVo.getLongitude())!=0) {
            long moveDis = (long) DistanceUtils.GetDistance(batteryInfoReqVo.getLongitude(), batteryInfoReqVo.getLatitude(), battery.getLockLongitude(),
                    battery.getLockLatitude());
            if (moveDis > MoveDis) {
                sendMovingMsg(battery);
            }
        }

        return new AsyncResult<Battery>(battery);
    }

    private BatteryInfoNst updateBatteryInfoNst(BatteryInfo btyInfo) {
        Integer batteryId = btyInfo.getBatteryId();
        BatteryInfoNst batteryInfoNst = batteryInfoNstMapper.selectByBtyId(batteryId);
        if (batteryInfoNst == null) {
            batteryInfoNst = new BatteryInfoNst();
            batteryInfoNst.setBatteryId(batteryId);
            copyProps(btyInfo, batteryInfoNst);
            batteryInfoNstMapper.insertSelective(batteryInfoNst);
        } else {
            copyProps(btyInfo, batteryInfoNst);
            batteryInfoNstMapper.updateByPrimaryKeySelective(batteryInfoNst);
        }

        return batteryInfoNst;

    }

    private void copyProps(BatteryInfo btyInfo, BatteryInfoNst batteryInfoNst) {
        BigDecimal longitude = new BigDecimal(btyInfo.getLongitude());
        BigDecimal latitude = new BigDecimal(btyInfo.getLatitude());
        if (BigDecimal.ZERO.compareTo(longitude) != 0) {
            batteryInfoNst.setLongitude(btyInfo.getLongitude());
        }
        if (BigDecimal.ZERO.compareTo(latitude) != 0) {
            batteryInfoNst.setLatitude(btyInfo.getLatitude());
        }
        batteryInfoNst.setTemperature(btyInfo.getTemperature());
        batteryInfoNst.setVoltage(btyInfo.getVoltage());
        batteryInfoNst.setStatus(btyInfo.getStatus());
        batteryInfoNst.setReceiveDate(new Date());
        batteryInfoNst.setSampleDate(new Date());

    }

    private String convertAdcToTemp(String adc) {
    	//增加温度超过60度时转换的部分
//        return TempUtils.isWarning(adc) ? adc : TempUtils.getTemp(adc);
    	return TempUtils.getTemp(adc);
    }

    private String convertAdcToVo(String adc) {
        float tem = Float.valueOf(adc);
        float vol = (float) ((int) ((tem / 10.73) * 10)) / 10;
        return String.valueOf(vol);
    }

    private BatteryStatus getBatteryStatus(BatteryInfoReq batteryInfoReqVo) {
        BatteryStatus status = BatteryStatus.NORMAL;
        String adcTmp = batteryInfoReqVo.getTemperature();
        if (TempUtils.isWarning(adcTmp)) {
            status = BatteryStatus.T_ABNORMAL;
        }

        float adcVol = Float.valueOf(convertAdcToVo(batteryInfoReqVo.getVoltage()));
        if (adcVol < 42 || adcVol > 77.5) {
            status = BatteryStatus.V_ABNORMAL;
        }

        return status;
    }

    private void sendWarningMsg(Battery battery) throws CrudException {

        UserBattery userBattery = userBatteryService.fetchUserByBtyId(battery.getId());
        User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
        userCodeService.sendWarningMsg(user.getMobilePhone(), battery.getImei());
    }

    private void sendVolageWarningMsg(Battery battery, String voltage,int flag) throws CrudException {

        UserBattery userBattery = userBatteryService.fetchUserByBtyId(battery.getId());
        User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
        userCodeService.sendWarningMsg(user.getMobilePhone(), battery.getImei(), voltage,flag);
    }

    private void sendMovingMsg(Battery battery) throws CrudException {

        UserBattery userBattery = userBatteryService.fetchUserByBtyId(battery.getId());
        User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
        userCodeService.sendMovingMsg(user.getMobilePhone(), battery.getImei());
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        logger.info("sendMovingMsgTime1:" + dateString);
    }
    
  //增加一个剪断信号线电压突变报警
    private void  sendViolentDestroyClient(Battery battery) throws CrudException{
    	UserBattery userBattery = userBatteryService.fetchUserByBtyId(battery.getId());
        User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
        userCodeService.sendViolentDestroyClient(user.getMobilePhone(), battery.getImei());
    }
    
    private void sendViolentDestroyService(String servciePhone,Battery battery) throws CrudException{
    	UserBattery userBattery = userBatteryService.fetchUserByBtyId(battery.getId());
        User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
        userCodeService.sendViolentDestroyService(servciePhone, battery.getImei(), user.getUserName(), user.getMobilePhone());
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
    public BatteryInfoNst fetchBtyInfo(String btySimNo) throws CrudException {
        Battery battery = batteryMapper.selectBySimNo(btySimNo);
        if (battery == null) {
            throw new FetchBtyInfoException("电池不存在");
        }

        BatteryInfoNst nstInfo = batteryInfoNstMapper.selectByBtyId(battery.getId());

        if (nstInfo != null) {
            return nstInfo;
        }
        BatteryInfo batteryInfo = batteryInfoMapper.selectByBtyId(battery.getId());
        if (batteryInfo == null) {
            throw new FetchBtyInfoException("未接收到此电池发送的信息");
        }
        return updateBatteryInfoNst(batteryInfo);

    }

    @Override
    public Battery fetchBtyById(int id) {
        return batteryMapper.selectByPrimaryKey(id);
    }

	@Override
	public int fetchRssi(int btyId) throws CrudException {
		 int rssi = 1;
		 BatteryInfoNst btyInfoNst = batteryInfoNstMapper.selectByBtyId(btyId);
		 if(btyInfoNst == null){
			 throw new FetchBtyInfoException("未接收到此电池发送的信息");
		 }
		 
		 BatteryInfo btyInfo = batteryInfoMapper.selectByBtyId(btyId);
		 if(btyInfo == null){
			 throw new FetchBtyInfoException("未接收到此电池发送的信息");
		 }
		 
		 Date now = new Date();
		 //最后一次上报数据的时间与现在间隔3分钟以内
		if(DateUtils.addMinutes(btyInfoNst.getReceiveDate(), 3).after(now)){
			if(btyInfoNst.getLongitude().equals(btyInfo.getLongitude())){
				rssi = 0;
			}else {
				rssi = 1;
			}
		}
		
		if(DateUtils.addMinutes(btyInfoNst.getReceiveDate(), 3).before(now) && DateUtils.addMinutes(btyInfoNst.getReceiveDate(), 30).after(now)){
			rssi = 1;
		}
		
		if(DateUtils.addMinutes(btyInfoNst.getReceiveDate(), 30).before(now)){
			rssi = 2;
		}
		
		return rssi;
	}

}
