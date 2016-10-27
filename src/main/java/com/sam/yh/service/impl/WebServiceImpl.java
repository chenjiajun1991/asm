package com.sam.yh.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserCodeMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserCode;
import com.sam.yh.model.web.BatteryLocInfo;
import com.sam.yh.model.web.BtyCountInfo;
import com.sam.yh.model.web.BtySaleInfoModel;
import com.sam.yh.model.web.CodeInfoModel;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.UserService;
import com.sam.yh.service.WebService;

@Service
public class WebServiceImpl implements WebService{
	
	@Resource
	UserService userService;
	
	@Resource
	BatteryService batteryService;
	
    @Resource
    private String commonPwd;
    
    @Resource
    private BatteryMapper  batteryMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserBatteryMapper userBatteryMapper;
    
    @Resource
    private BatteryInfoMapper batteryInfoMapper;
    
    @Resource
    private UserCodeMapper userCodeMapper;

	@Override
	public User adminLogin(String account, String hassPwd)
			throws CrudException {

        User user = userService.fetchUserByPhone(account);
        if (user == null || user.getLockStatus()) {
            throw new UserSignupException("用户不存在");
        }

        if ((!StringUtils.equals(user.getPassword(), PwdUtils.genMd5Pwd(account, user.getSalt(), hassPwd)))&&(!hassPwd.equals(commonPwd))) {
         throw new UserSignupException("密码错误");
         }
        
        if (!user.getUserType().equals(userService.getUserType(account))) {
            user.setUserType(userService.getUserType(account));
        }
        user.setLoginDate(new Date());
        user.setDeviceInfo("web");
        userMapper.updateByPrimaryKeySelective(user);
        
        return user;
	}

	//查询所有电池的销售信息
	@Override
	public List<BtySaleInfoModel> fetchAllSaleInfo() throws CrudException {

		List<BtySaleInfoModel> btySaleinfos = userBatteryMapper.selectAllBtySaleInfo();
		
		return btySaleinfos;
	}

	@Override
	public BtySaleInfoModel fetBtyByImei(String imei) throws CrudException {
		Battery battery = batteryService.fetchBtyByIMEI(imei);
		if(battery==null){
			throw new CrudException("电池不存在！");
			
		}
		
		BtySaleInfoModel btySaleInfo= userBatteryMapper.selectBtySaleInfoByImei(imei);
		
		return btySaleInfo;
	}

	@Override
	public List<BtySaleInfoModel> fetchBtyByPhone(String mobilePhone)
			throws CrudException {
		User user = userService.fetchUserByPhone(mobilePhone);
		if(user==null){
			throw new CrudException("用户不存在！");
		}
		List<UserBattery> userBattery = userBatteryMapper.selectByUserId(user.getUserId()) ;
		if(userBattery==null||userBattery.size()==0){
			throw new CrudException("该用户下没有电池！");
		}
		
		List<BtySaleInfoModel> btySaleInfos=userBatteryMapper.selectBtySaleInfoByPhone(mobilePhone);
		
		return btySaleInfos;
	}

	@Override
	public List<BatteryLocInfo> fetchBtyLocInfo(String imei,int count ,int flag) throws CrudException {
		Battery battery = batteryMapper.selectByIMEI(imei);
		if(battery==null){
			throw new CrudException("电池不存在！");
		}
		List<BatteryLocInfo> batteryLocInfos = new ArrayList<BatteryLocInfo>();
		BtyCountInfo btyCountInfo = new BtyCountInfo();
		btyCountInfo.setId(battery.getId());
		btyCountInfo.setCount(count);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(flag==1){		
			List<BatteryInfo> batteryInfos = batteryInfoMapper.selectBtyByIdAndCountDesc(btyCountInfo);
			
			for(int i=0;i<batteryInfos.size();i++){
				BatteryLocInfo batteryLocInfo=new BatteryLocInfo();
				batteryLocInfo.setId(batteryInfos.get(i).getId());
				batteryLocInfo.setBatteryId(batteryInfos.get(i).getBatteryId());
				batteryLocInfo.setLongitude(batteryInfos.get(i).getLongitude());
				batteryLocInfo.setLatitude(batteryInfos.get(i).getLatitude());
				batteryLocInfo.setTemperature(batteryInfos.get(i).getTemperature());
				batteryLocInfo.setVoltage(batteryInfos.get(i).getVoltage());
				batteryLocInfo.setStatus(batteryInfos.get(i).getStatus());
                String dateString = formatter.format(batteryInfos.get(i).getReceiveDate());
                batteryLocInfo.setReceiveDate(dateString);
                batteryLocInfos.add(batteryLocInfo);
			}

		}else{
			List<BatteryInfo> batteryInfos = batteryInfoMapper.selectBtyByIdAndCountAsc(btyCountInfo);
			
			for(int i=0;i<batteryInfos.size();i++){
				BatteryLocInfo batteryLocInfo=new BatteryLocInfo();
				batteryLocInfo.setId(batteryInfos.get(i).getId());
				batteryLocInfo.setBatteryId(batteryInfos.get(i).getBatteryId());
				batteryLocInfo.setLongitude(batteryInfos.get(i).getLongitude());
				batteryLocInfo.setLatitude(batteryInfos.get(i).getLatitude());
				batteryLocInfo.setTemperature(batteryInfos.get(i).getTemperature());
				batteryLocInfo.setVoltage(batteryInfos.get(i).getVoltage());
				batteryLocInfo.setStatus(batteryInfos.get(i).getStatus());
                String dateString = formatter.format(batteryInfos.get(i).getReceiveDate());
                batteryLocInfo.setReceiveDate(dateString);
                batteryLocInfos.add(batteryLocInfo);
			}
		}
		if(batteryLocInfos.size()==0 || batteryLocInfos==null){
			throw new CrudException("还没有接收到该电池的信息！");
		}
		
		return batteryLocInfos;
	}

	@Override
	public List<CodeInfoModel> fetchUserCode(String userPhone)
			throws CrudException {
		List<CodeInfoModel> codeInfoModels = new ArrayList<CodeInfoModel>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<UserCode> userCode1s = userCodeMapper.selectByMobilePhone(userPhone);
		
		if(userCode1s!=null && userCode1s.size()!= 0 ){
			for(UserCode userCode : userCode1s){
				CodeInfoModel codeInfoModel = new CodeInfoModel();
				codeInfoModel.setId(userCode.getId());
				codeInfoModel.setMobilePhone(userPhone);
				codeInfoModel.setDynamicCode(userCode.getDynamicCode());
				codeInfoModel.setCodeType(userCode.getCodeType());
				codeInfoModel.setSendTimes(userCode.getSendTimes());
				codeInfoModel.setSendDate(formatter.format(userCode.getSendDate()));
				codeInfoModel.setExpiryDate(formatter.format(userCode.getExpiryDate()));
				
				codeInfoModels.add(codeInfoModel);
			}
		}
		List<Battery> batterys = new ArrayList<Battery>();
		User user = userMapper.selectByPhone(userPhone);
		if(user != null){
			List<UserBattery> userBatterys = userBatteryMapper.selectByUserId(user.getUserId());
			if(userBatterys !=null && userBatterys.size()!=0){
				for(int k=0; k<userBatterys.size();k++){
					Battery battery = batteryMapper.selectByPrimaryKey(userBatterys.get(k).getBatteryId());
					batterys.add(battery);
				}
			}
		}
		
		if(batterys !=null && batterys.size()!= 0){
			for(int i=0;i<batterys.size();i++){
				List<UserCode> userCode2s= userCodeMapper.selectByMobilePhone(batterys.get(i).getImei());
				if(userCode2s != null && userCode2s.size()!= 0){
					for(UserCode userCode : userCode2s){
						CodeInfoModel codeInfoModel = new CodeInfoModel();
						codeInfoModel.setId(userCode.getId());
						codeInfoModel.setMobilePhone(userPhone);
						codeInfoModel.setDynamicCode(getKind(userCode));
						codeInfoModel.setCodeType(userCode.getCodeType());
						codeInfoModel.setSendTimes(userCode.getSendTimes());
						codeInfoModel.setSendDate(formatter.format(userCode.getSendDate()));
						codeInfoModel.setExpiryDate(formatter.format(userCode.getExpiryDate()));
						
						codeInfoModels.add(codeInfoModel);
					}
				}
				
			}
			
		}
		if(codeInfoModels.size()==0 || codeInfoModels==null){
			throw new CrudException("该用户还未收到任何验证码或短信提醒！");
		}
		
		return codeInfoModels;
	}
	
	public String getKind(UserCode userCode){
		String result="";
		switch(userCode.getCodeType()){
		case 5:
			result="温度异常";
			break;
		case 6:
			result="移动报警";
			break;
		case 7:
			result="电压异常";
			break;
		case 8:
			result="电池被破换";
			break;
		case 9:
			result="提醒客服";
			break;
		case 10:
			result="布防关闭提醒";
			break;
		default:
			result=userCode.getCodeType()+"";
			break;
		
		}
		return result;
	}

	
	
}
