package com.sam.yh.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.ResellerMapper;
import com.sam.yh.dao.TroubleBatteryMapper;
import com.sam.yh.dao.TroubleUserBatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserCodeMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.Reseller;
import com.sam.yh.model.TroubleBattery;
import com.sam.yh.model.TroubleUserBattery;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserCode;
import com.sam.yh.model.web.BatteryLocInfo;
import com.sam.yh.model.web.BtyCountInfo;
import com.sam.yh.model.web.BtySaleInfoModel;
import com.sam.yh.model.web.CodeInfoModel;
import com.sam.yh.model.web.TroubleBtyInfo;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.UserService;
import com.sam.yh.service.WebService;

@Service
public class WebServiceImpl implements WebService{
  private static final Logger logger = LoggerFactory.getLogger(WebServiceImpl.class);
	
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
    
    @Resource
    private TroubleBatteryMapper troubleBatteryMapper;
    
    @Resource
    private TroubleUserBatteryMapper troubleUserBatteryMapper;
    
    @Resource
    private ResellerMapper resellerMapper;

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
		TroubleBattery troubleBattery = troubleBatteryMapper.selectBtyByIMEI(imei);
		
		if(battery==null && troubleBattery == null){
				throw new CrudException("电池不存在！");
		}
		
		List<BatteryLocInfo> batteryLocInfos = new ArrayList<BatteryLocInfo>();
		BtyCountInfo btyCountInfo = new BtyCountInfo();
		
		if(battery != null){
			btyCountInfo.setId(battery.getId());
		}else{
			btyCountInfo.setId(troubleBattery.getBatteryId());
		}
		
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

	@Override
	public void removeBattery(String imei,String reason) throws CrudException {
		Battery battery = batteryMapper.selectByIMEI(imei);
		if(battery==null){
			throw new CrudException("电池不存在！");
		}

		UserBattery userBattery = userBatteryMapper.selectByBtyId(battery.getId());
		if(userBattery == null){
			throw new CrudException("电池还没有绑定！");
		}
		
	     Date now = new Date();
		
		TroubleBattery troubleBattery = new TroubleBattery();
		
		troubleBattery.setBatteryId(battery.getId());
		troubleBattery.setImei(battery.getImei());
		troubleBattery.setSimNo(battery.getSimNo());
		troubleBattery.setSn(battery.getSn());
		troubleBattery.setBtyQuantity(battery.getBtyQuantity());
		troubleBattery.setResellerId(battery.getResellerId());
		troubleBattery.setSaleDate(battery.getSaleDate());
		troubleBattery.setRemoveDate(now);
		troubleBattery.setReason(reason);

		troubleBatteryMapper.insert(troubleBattery);
		
		TroubleUserBattery troubleUserBattery = new TroubleUserBattery();
		
		troubleUserBattery.setBatteryId(userBattery.getBatteryId());
		troubleUserBattery.setUserId(userBattery.getUserId());
		troubleUserBattery.setRemoveDate(now);

		 troubleUserBatteryMapper.insert(troubleUserBattery);
		
		batteryMapper.deleteByPrimaryKey(battery.getId());
		userBatteryMapper.deleteByBtyId(userBattery.getBatteryId());
				
	}

	@Override
	public List<TroubleBtyInfo> fetchAllTroBty() throws CrudException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<TroubleBtyInfo> troubleBtyInfos = new ArrayList<TroubleBtyInfo>();
		
		List<TroubleBattery> troubleBatteries = troubleBatteryMapper.selectAllTroubleBty();
		if(troubleBatteries==null || troubleBatteries.size()==0){
			throw new CrudException("还没有解绑电池信息！");
		}
		for(TroubleBattery troubleBattery : troubleBatteries){
			TroubleUserBattery troubleUserBattery = troubleUserBatteryMapper.selectByBtyId(troubleBattery.getBatteryId());
			if(troubleUserBattery != null){
				User user = userMapper.selectByPrimaryKey(troubleUserBattery.getUserId());
				User reseller = userMapper.selectByPrimaryKey(troubleBattery.getResellerId());
				
				if(user != null && reseller != null){
					TroubleBtyInfo troubleBtyInfo = new TroubleBtyInfo();
					
					troubleBtyInfo.setBtyId(troubleBattery.getBatteryId());
					troubleBtyInfo.setBtyImei(troubleBattery.getImei());
					troubleBtyInfo.setBtySimNo(troubleBattery.getSimNo());
					troubleBtyInfo.setBtySn(troubleBattery.getSn());
					troubleBtyInfo.setBtyQuantity(troubleBattery.getBtyQuantity());
					troubleBtyInfo.setUserName(user.getUserName());
					troubleBtyInfo.setUserphone(user.getMobilePhone());
					troubleBtyInfo.setResellerName(reseller.getUserName());
					troubleBtyInfo.setResellerPhone(reseller.getMobilePhone());
					
					String dateSaleDate=formatter.format(troubleBattery.getSaleDate());
					String dateRemoveDate = formatter.format(troubleBattery.getRemoveDate());
					troubleBtyInfo.setSaleDate(dateSaleDate);
					troubleBtyInfo.setRemoveDate(dateRemoveDate);
					troubleBtyInfo.setReason(troubleBattery.getReason());
					
					troubleBtyInfos.add(troubleBtyInfo);
				}
			}
		}
		
		if(troubleBtyInfos==null || troubleBtyInfos.size()==0){
			throw new CrudException("还没有解绑电池信息！");
		}
		
		
		return troubleBtyInfos;
	}

	@Override
	public List<TroubleBtyInfo> fetchTroBtyByImeiOrPhone(String parms)
			throws CrudException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<TroubleBtyInfo> troubleBtyInfos = new ArrayList<TroubleBtyInfo>();
		
		if(parms.length()==11){
			User user = userMapper.selectByPhone(parms);
			if(user==null){
				throw new CrudException("用户不存在！");
			}
		    List<TroubleUserBattery> troubleUserBatteries = troubleUserBatteryMapper.selectByUserId(user.getUserId());
		    
		    if(troubleUserBatteries==null || troubleUserBatteries.size()==0){
		    	throw new CrudException("没有查询到该用户的解绑记录！");
		    }
		    
		    for(TroubleUserBattery troubleUserBattery : troubleUserBatteries){
		    	TroubleBattery troubleBattery = troubleBatteryMapper.selectBtyById(troubleUserBattery.getBatteryId());
		    	if(troubleBattery!= null){
		    		User reseller = userMapper.selectByPrimaryKey(troubleBattery.getResellerId());
		    		
		    		
		    		TroubleBtyInfo troubleBtyInfo = new TroubleBtyInfo();
		    		
		    		troubleBtyInfo.setBtyId(troubleBattery.getBatteryId());
		    		troubleBtyInfo.setBtyImei(troubleBattery.getImei());
		    	    troubleBtyInfo.setBtySimNo(troubleBattery.getSimNo());
		    	    troubleBtyInfo.setBtySn(troubleBattery.getSn());
		    	    troubleBtyInfo.setBtyQuantity(troubleBattery.getBtyQuantity());
		    	    troubleBtyInfo.setUserName(user.getUserName());
		    	    troubleBtyInfo.setUserphone(user.getMobilePhone());
		    	    troubleBtyInfo.setResellerName(reseller.getUserName());
		    	    troubleBtyInfo.setResellerPhone(reseller.getMobilePhone());
		    	    String stringSaleDate = formatter.format(troubleBattery.getSaleDate());
		    	    String stringRemoveDate = formatter.format(troubleBattery.getRemoveDate());
		    	    troubleBtyInfo.setSaleDate(stringSaleDate);
		    	    troubleBtyInfo.setRemoveDate(stringRemoveDate);
		    	    troubleBtyInfo.setReason(troubleBattery.getReason());
		    	    
		    	    troubleBtyInfos.add(troubleBtyInfo);
		    	    
		    	}
		    	
		    }
			
		}
		
		if(parms.length()==15 || parms.length()==20){
			TroubleBattery troubleBattery = troubleBatteryMapper.selectBtyByIMEI(parms);
			if(troubleBattery==null){
				throw new CrudException("解绑的电池中没有该电池！");
			}
			TroubleUserBattery troubleUserBattery = troubleUserBatteryMapper.selectByBtyId(troubleBattery.getBatteryId());
			
			if(troubleUserBattery == null){
				throw new CrudException("解绑电池没有查到对应的用户信息!");
			}
			 User user = userMapper.selectByPrimaryKey(troubleUserBattery.getUserId());
			 User reseller = userMapper.selectByPrimaryKey(troubleBattery.getResellerId());
			 
			 TroubleBtyInfo troubleBtyInfo = new TroubleBtyInfo();
			 
			    troubleBtyInfo.setBtyId(troubleBattery.getBatteryId());
	    		troubleBtyInfo.setBtyImei(troubleBattery.getImei());
	    	    troubleBtyInfo.setBtySimNo(troubleBattery.getSimNo());
	    	    troubleBtyInfo.setBtySn(troubleBattery.getSn());
	    	    troubleBtyInfo.setBtyQuantity(troubleBattery.getBtyQuantity());
	    	    troubleBtyInfo.setUserName(user.getUserName());
	    	    troubleBtyInfo.setUserphone(user.getMobilePhone());
	    	    troubleBtyInfo.setResellerName(reseller.getUserName());
	    	    troubleBtyInfo.setResellerPhone(reseller.getMobilePhone());
	    	    String stringSaleDate = formatter.format(troubleBattery.getSaleDate());
	    	    String stringRemoveDate = formatter.format(troubleBattery.getRemoveDate());
	    	    troubleBtyInfo.setSaleDate(stringSaleDate);
	    	    troubleBtyInfo.setRemoveDate(stringRemoveDate);
	    	    troubleBtyInfo.setReason(troubleBattery.getReason());
	    	    
	    	    troubleBtyInfos.add(troubleBtyInfo);
		}
		
		if(troubleBtyInfos == null || troubleBtyInfos.size() == 0){
			throw new CrudException("没有查询到解绑电池的信息！");
		}
		return troubleBtyInfos;
	}

	@Override
	public void updateBattery(int btyId, String btyImei, String btySimNO,
			String btySn, int btyQuantity) throws CrudException {
		Battery battery = batteryMapper.selectByPrimaryKey(btyId);
		if(battery == null){
			throw new CrudException("电池不存在！");
		}
		if(!btyImei.equals(battery.getImei())){
			Battery b1 = batteryMapper.selectByIMEI(btyImei);
			if(b1 != null){
				throw new CrudException("该Imei号已被其他电池使用！");
			}
		}
		if(!btySimNO.equals(battery.getSimNo())){
			Battery b2 = batteryMapper.selectBySimNo(btySimNO);
			if(b2!= null){
				throw new CrudException("该sim卡号已被其他电池使用！");
			}
		}
		if(!btySn.equals(battery.getSn())){
			Battery b3 = batteryMapper.selectBySn(btySn);
			if(b3 != null){
				throw new CrudException("该电池的系列号已被其他电池使用！");
			}
		}
		
		battery.setImei(btyImei);
		battery.setSimNo(btySimNO);
		battery.setSn(btySn);
		battery.setBtyQuantity(btyQuantity);
		
		batteryMapper.updateByPrimaryKey(battery);		
		
	}

	@Override
	public void updateUserBattery(int btyId, String userName, String userPhone)
			throws CrudException {
		Battery battery = batteryMapper.selectByPrimaryKey(btyId);
		UserBattery userBattery = userBatteryMapper.selectByBtyId(btyId);
		if(battery == null || userBattery == null){
			batteryMapper.deleteByPrimaryKey(btyId);
			userBatteryMapper.deleteByBtyId(btyId);
			throw new CrudException("该电池还没有绑定，无需更改,重新绑定即可！");
		}
		
		User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
		if(user == null){
			User user1 =new User();
			user1.setUserId(userBattery.getUserId());
			user1.setMobilePhone(userPhone);
			user1.setUserName(userName);
			user1.setUserType("0");
			user1.setSalt("z2m85Jmhwh");
			user1.setPassword(PwdUtils.genMd5Pwd(userPhone, "z2m85Jmhwh", "88888888"));
		    userMapper.insert(user1);
		}
		
		if(user!=null){
			User user2 = userMapper.selectByPhone(userPhone);
			if(user2 == null){
				user.setMobilePhone(userPhone);
				user.setUserName(userName);
				userMapper.updateByPrimaryKey(user);	
			}else{
				user2.setUserName(userName);
				userMapper.updateByPrimaryKey(user2);
				userBattery.setUserId(user2.getUserId());
				userBatteryMapper.updateByBtyId(userBattery);

			}
		}
		
	}

	@Override
	public void updateBtyReseller(int btyId, String resellerName,
			String resellerPhone) throws CrudException {
		Battery battery = batteryMapper.selectByPrimaryKey(btyId);
		if(battery == null){
			throw new CrudException("电池不存在！");
		}
		User user = userMapper.selectByPhone(resellerPhone);
		if(user == null){
			throw new CrudException("该经销商未注册，请先注册！");
		}
		Reseller reseller =  resellerMapper.selectByPrimaryKey(user.getUserId());
	
		if(reseller == null){
			throw new CrudException("该用户不是经销商！");
		}
		
		if(battery.getResellerId() == reseller.getUserId()){
			user.setUserName(resellerName);
			userMapper.updateByPrimaryKey(user);
		}
		if(battery.getResellerId() != reseller.getUserId()){
			battery.setResellerId(reseller.getUserId());
			batteryMapper.updateByPrimaryKey(battery);	
		}
	}

}
