package com.sam.yh.clock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;





import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryInfoNstMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.BatteryInfoNst;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.web.BtyCountInfo;
import com.sam.yh.service.UserCodeService;

@Component
public class CheckErrBtyClockImpl implements CheckErrBtyClock{
	
	@Resource
	private BatteryMapper batteryMapper;
	
	@Resource
	private BatteryInfoMapper batteryInfoMapper;
	
	@Resource
	private BatteryInfoNstMapper batteryInfoNstMapper;
	
	@Resource
	private UserBatteryMapper userBatteryMapper;
	
	@Resource
	private UserMapper userMapper;
	
    @Resource
    private String servicePhone;
	
	@Resource
	private UserCodeService userCodeService;
	
	private static int delayHour = 3;
	private static int checkCount = 480*10;
	private static float warnVoltage = 75f;
	private static float warnTemputer = 80f;
	
    private static final Logger logger = LoggerFactory.getLogger(CheckErrBtyClockImpl.class);
    
	@Scheduled(cron="0 0 */3 * * ?")   //每3个小时执行一次
	@Override
	public void fetchErr() throws CrudException {
		// TODO Auto-generated method stub
		
		List<Battery> batterys =  batteryMapper.selectAllBty();
		
		if(batterys != null){
			for(Battery battery : batterys){
				
				
				BatteryInfoNst batteryInfoNst = batteryInfoNstMapper.selectByBtyId(battery.getId());
				
				if(batteryInfoNst != null){
					
					if(Float.parseFloat(batteryInfoNst.getVoltage()) < 20f){
						
						Date now = new Date();
						
						Date receiveDate = batteryInfoNst.getReceiveDate();
						
						if(now.after(DateUtils.addHours(receiveDate, delayHour))){
							
							UserBattery userBattery = userBatteryMapper.selectByBtyId(battery.getId());
							
							 User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
							
							if(userBattery != null){
								
								BtyCountInfo btyCountInfo = new BtyCountInfo();
								
								btyCountInfo.setId(battery.getId());
								btyCountInfo.setCount(checkCount);
								
								List <Float> voltages = new ArrayList<>();
								
								List <Float> temperatures = new ArrayList<>();
								
								List<BatteryInfo> batteryInfos = batteryInfoMapper.selectBtyByIdAndCountDesc(btyCountInfo);
								
								if(batteryInfos != null){
									
									for(BatteryInfo batteryInfo : batteryInfos){
										
										float voltage = Float.parseFloat(batteryInfo.getVoltage());
										
										float temperature = Float.parseFloat(batteryInfo.getTemperature());
										
										voltages.add(voltage);
										
										temperatures.add(temperature);
										
									}	
								}
								
								if(voltages != null && temperatures != null){
									
									if(checkVoltage(voltages) || checkTemputer(temperatures)){
										
										
										// do something
										userCodeService.sendWarnFire(servicePhone, battery.getImei(), user.getUserName(), user.getMobilePhone());
										
										logger.info("warning fire:" + battery.getImei());
										
										
									}
									
									
								}	
								
							}
							
						}
						
						
					}
					
					
					

				}
				
			}
		}
		
	}
	
	
	private boolean checkVoltage(List<Float> parms){
		
		for(Float p :parms){
			if(p >= warnVoltage){
				return true;
			}
		}
		
		return false;	
	}
	
 private boolean checkTemputer(List<Float> parms){
		
		for(Float p :parms){
			if(p >= warnTemputer){
				return true;
			}
		}	
		return false;	
	}
	
	

}
