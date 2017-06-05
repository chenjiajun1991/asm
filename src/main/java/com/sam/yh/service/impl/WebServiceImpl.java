package com.sam.yh.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryInfoNstMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.ResellerMapper;
import com.sam.yh.dao.TroubleBatteryMapper;
import com.sam.yh.dao.TroubleUserBatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserCodeMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.BatteryInfoNst;
import com.sam.yh.model.Reseller;
import com.sam.yh.model.TroubleBattery;
import com.sam.yh.model.TroubleUserBattery;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserCode;
import com.sam.yh.model.web.BatteryLocInfo;
import com.sam.yh.model.web.BreakBtyInfo;
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
    private String superAdmin;
    
    @Resource
    private BatteryMapper  batteryMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserBatteryMapper userBatteryMapper;
    
    @Resource
    private BatteryInfoMapper batteryInfoMapper;
    
    @Resource
    private BatteryInfoNstMapper batteryInfoNstMapper;
    
    @Resource
    private UserCodeMapper userCodeMapper;
    
    @Resource
    private TroubleBatteryMapper troubleBatteryMapper;
    
    @Resource
    private TroubleUserBatteryMapper troubleUserBatteryMapper;
    
    @Resource
    private ResellerMapper resellerMapper;
    
    private static final int testResellerId = 24;
    
    private static final int disconnetHour = 3;
    
    private static final String NOT_RECEIVED = "未收到电池信息";
    
    private static final String DISCONNET = "离线";
    
    private static final String SINGLE_DROP = "单落";
    
    private static final String BROWNOUT = "电压过低";
    
    private static final String OVERTENSION = "电压过高";
    
    private static final String EXCESS_TEMPUTER = "温度过高";
    
    private static final String WRAN_FIRE = "着火风险";
    

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
		List<TroubleBattery> troubleBatteryList = troubleBatteryMapper.selectBtyByIMEI(imei);
		
		if(battery==null && troubleBatteryList == null){
				throw new CrudException("电池不存在！");
		}
		
		List<BatteryLocInfo> batteryLocInfos = new ArrayList<BatteryLocInfo>();
		BtyCountInfo btyCountInfo = new BtyCountInfo();
		
		if(battery != null){
			btyCountInfo.setId(battery.getId());
		}else{
			if(troubleBatteryList.size()>0){
				
				btyCountInfo.setId(troubleBatteryList.get(troubleBatteryList.size()-1).getBatteryId());
				
			}else{
				throw new CrudException("电池不存在！");
			}
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
			List<TroubleBattery> troubleBatteryList = troubleBatteryMapper.selectBtyByIMEI(parms);
			if(troubleBatteryList==null){
				throw new CrudException("解绑的电池中没有该电池！");
			}
			
			for(TroubleBattery troubleBattery : troubleBatteryList){
				
				TroubleUserBattery troubleUserBattery = troubleUserBatteryMapper.selectByBtyId(troubleBattery.getBatteryId());
				
				if(troubleUserBattery != null){
					
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
				
			}	 
			    
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

	@Override
	public void deleteBattery(String admin) throws CrudException {
		// TODO Auto-generated method stub
		if(!admin.endsWith(superAdmin)){
			throw new CrudException("该功能暂未开放！");
		}
		
	}
	

	@Override
	public List<BtySaleInfoModel> fetchResellerSaleInfo(String resellerPhone)
			throws CrudException {
		// TODO Auto-generated method stub
		
        List<BtySaleInfoModel> btySaleinfos = userBatteryMapper.selectAllBtySaleInfo();
        
        List<BtySaleInfoModel> btyResellerSaleInfos = new ArrayList<BtySaleInfoModel>();
        
        for(BtySaleInfoModel btySaleinfo : btySaleinfos){
        	
        	if(btySaleinfo.getResellerPhone().equals(resellerPhone)){
        		btyResellerSaleInfos.add(btySaleinfo);
        	}
        	
        }
		
		return btyResellerSaleInfos;
		
	}

	@Override
	public List<BreakBtyInfo> fetchAllBreakBtyInfo() throws CrudException {
		// TODO Auto-generated method stub
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<BreakBtyInfo> breakBtyInfos = new ArrayList<BreakBtyInfo>();
		
		List<Battery> batteryList = batteryMapper.selectAllBty();
		
		for(Battery battery : batteryList){
			
			if(battery.getResellerId() != testResellerId){
				
				UserBattery userBattery = userBatteryMapper.selectByBtyId(battery.getId());
				
				if(userBattery != null){
					
					User user = userMapper.selectByPrimaryKey(userBattery.getUserId());
					User reseller = userMapper.selectByPrimaryKey(battery.getResellerId());
					
					if(user != null && reseller != null){
						
						BreakBtyInfo breakBtyInfo = new BreakBtyInfo();
						
						breakBtyInfo.setBtyId(battery.getId());
						breakBtyInfo.setBtyImei(battery.getImei());
						breakBtyInfo.setBtySimNo(battery.getSimNo());
						breakBtyInfo.setBtySn(battery.getSn());
						breakBtyInfo.setBtyQuantity(battery.getBtyQuantity());
						breakBtyInfo.setUserName(user.getUserName());
						breakBtyInfo.setUserphone(user.getMobilePhone());
						breakBtyInfo.setResellerName(reseller.getUserName());
						breakBtyInfo.setResellerPhone(reseller.getMobilePhone());
						breakBtyInfo.setSaleDate(formatter.format(battery.getSaleDate()));
							
						BatteryInfoNst batteryInfoNst = batteryInfoNstMapper.selectByBtyId(battery.getId());
						if(batteryInfoNst == null){
							breakBtyInfo.setLongitude("--");
							breakBtyInfo.setLatitude("--");
							breakBtyInfo.setVoltage("--");
						    breakBtyInfo.setTemperature("--");
						    breakBtyInfo.setReceiveDate("--");
						    breakBtyInfo.setMsg(NOT_RECEIVED);
							
						    breakBtyInfos.add(breakBtyInfo);
						}else{
							
							breakBtyInfo.setLongitude(batteryInfoNst.getLongitude());
							breakBtyInfo.setLatitude(batteryInfoNst.getLatitude());
							breakBtyInfo.setVoltage(batteryInfoNst.getVoltage());
						    breakBtyInfo.setTemperature(batteryInfoNst.getTemperature());
						    breakBtyInfo.setReceiveDate(formatter.format(batteryInfoNst.getReceiveDate()));
						    
						    Date now = new Date();
						    if(now.after(DateUtils.addHours(batteryInfoNst.getReceiveDate(), disconnetHour))){
						    	
						    	String msg =  DISCONNET;
						    	
						    	if(Float.parseFloat(batteryInfoNst.getVoltage())< 15 ){
						    		msg = msg +","+SINGLE_DROP+","+WRAN_FIRE;
						    	}
						    	
						    	
						    	if(Float.parseFloat(batteryInfoNst.getTemperature()) > 65){
						    		msg = msg +","+EXCESS_TEMPUTER;
						    	}
						    	
						    	if(battery.getBtyQuantity() == 4){
						    		
						    		if(Float.parseFloat(batteryInfoNst.getVoltage())> 15  && Float.parseFloat(batteryInfoNst.getVoltage())< 42){
						    			
						    			msg = msg +","+BROWNOUT;
						    			
						    		}
						    		
						    		if(Float.parseFloat(batteryInfoNst.getVoltage()) > 62){
						    			msg = msg +","+OVERTENSION;
						    		}
						    	}
						    	
						    	if(battery.getBtyQuantity() == 5){
						    		
                                    if(Float.parseFloat(batteryInfoNst.getVoltage())> 15  && Float.parseFloat(batteryInfoNst.getVoltage())< 52.5){
						    			
						    			msg = msg +","+BROWNOUT;
						    			
						    		}
						    		
						    		if(Float.parseFloat(batteryInfoNst.getVoltage()) > 75){
						    			msg = msg +","+OVERTENSION;
						    		}
						    		
						    	}
						    	 breakBtyInfo.setMsg(msg);
						    	 breakBtyInfos.add(breakBtyInfo);
						    		
						    }else{
						    	
						    	
                                String msg =  "";
						    	
						    	if(Float.parseFloat(batteryInfoNst.getVoltage())< 15 ){
						    		msg = msg +","+SINGLE_DROP;
						    	}
						    	
						    	
						    	if(Float.parseFloat(batteryInfoNst.getTemperature()) > 65){
						    		msg = msg +","+EXCESS_TEMPUTER;
						    	}
						    	
						    	if(battery.getBtyQuantity() == 4){
						    		
						    		if(Float.parseFloat(batteryInfoNst.getVoltage())> 15  && Float.parseFloat(batteryInfoNst.getVoltage())< 42){
						    			
						    			msg = msg +","+BROWNOUT;
						    			
						    		}
						    		
						    		if(Float.parseFloat(batteryInfoNst.getVoltage()) > 62){
						    			msg = msg +","+OVERTENSION;
						    		}
						    	}
						    	
						    	if(battery.getBtyQuantity() == 5){
						    		
                                    if(Float.parseFloat(batteryInfoNst.getVoltage())> 15  && Float.parseFloat(batteryInfoNst.getVoltage())< 52.5){
						    			
						    			msg = msg +","+BROWNOUT;
						    			
						    		}
						    		
						    		if(Float.parseFloat(batteryInfoNst.getVoltage()) > 75){
						    			msg = msg +","+OVERTENSION;
						    		}
						    		
						    	}
						    	
						    	if(msg.length() > 0){
						    		breakBtyInfo.setMsg(msg);
						    		breakBtyInfos.add(breakBtyInfo);
						    	}
						    	
						    }
						    
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return breakBtyInfos;
	}

	@Override
	public List<BreakBtyInfo> fetchBreakBtyInfoByReseller(String resellerPhone)
			throws CrudException {

       User user = userMapper.selectByPhone(resellerPhone);
       
       if(user == null){
    	   throw new CrudException("经销商不存在！");
       }
       
       Reseller reseller = resellerMapper.selectByPrimaryKey(user.getUserId());
       
       if(reseller == null){
    	   throw new CrudException("该用户不是经销商！");
       }
       
       List<BreakBtyInfo> breakBtyInfos = new ArrayList<BreakBtyInfo>();
       
       
       List<BreakBtyInfo> breakBtyInfoList = fetchAllBreakBtyInfo();
       
       
       for(BreakBtyInfo breakBtyInfo : breakBtyInfoList){
    	   Battery battery = batteryMapper.selectByIMEI(breakBtyInfo.getBtyImei());
    	   
    	   
    	   if(battery.getResellerId() .equals(user.getUserId())){
    		   breakBtyInfos.add(breakBtyInfo);   
    	   }
    	   
       }
		
		return breakBtyInfos;
	}

}
