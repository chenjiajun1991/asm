package com.sam.yh.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtysException;
import com.sam.yh.crud.exception.FetchFollowerException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.PubBattery;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryInfo;
import com.sam.yh.model.UserBatteryKey;
import com.sam.yh.model.UserFollow;
import com.sam.yh.resp.bean.BtyFollower;
import com.sam.yh.resp.bean.BtySaleInfo;
import com.sam.yh.service.UserBatteryService;

@Service
public class UserBatteryServiceImpl implements UserBatteryService {
	

    @Resource
    UserBatteryMapper userBatteryMapper;

    @Resource
    UserFollowMapper userFollowMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    BatteryMapper batteryMapper;
    
    @Resource
    BatteryInfoMapper batteryInfoMapper;
    

    @Override
    public List<UserBattery> fetchUserBattery(int userId) {
        return userBatteryMapper.selectByUserId(userId);
    }

    @Override
    public List<UserFollow> fetchUserFollowBty(int userId) {
        return userFollowMapper.selectByUserId(userId);
    }

    @Override
    public UserBattery fetchUserByBtyId(int batteryId) {
        return userBatteryMapper.selectByBtyId(batteryId);
    }

    @Override
    public List<PubBattery> fetchMyBtys(String mobilePhone) throws CrudException {
        User user = userMapper.selectByPhone(mobilePhone);
        if (user == null) {
            throw new FetchBtysException("用户不存在");
        }

        return userBatteryMapper.selectBtysByUserId(user.getUserId());

    }

    @Override
    public List<PubBattery> fetchfriendBtys(String mobilePhone) throws CrudException {
        User user = userMapper.selectByPhone(mobilePhone);
        if (user == null) {
            throw new FetchBtysException("用户不存在");
        }

        return userFollowMapper.selectBtysByUserId(user.getUserId());
    }

    @Override
    public List<BtyFollower> fetchBtyFollowers(String userName, String btyPubSn) throws CrudException {
        User user = userMapper.selectByPhone(userName);
        if (user == null) {
            throw new FetchFollowerException("用户不存在");
        }
        Battery battery = batteryMapper.selectByPubSn(btyPubSn);
        if (battery == null) {
            throw new FetchFollowerException("电池不存在");
        }

        UserBatteryKey key = new UserBatteryKey();
        key.setUserId(user.getUserId());
        key.setBatteryId(battery.getId());
        UserBattery userBattery = userBatteryMapper.selectByPrimaryKey(key);
        if (userBattery == null) {
            throw new FetchFollowerException("您未购买此电池");
        }

        return userFollowMapper.selectBtyFollowers(battery.getId());
    }

    
    //查询当天销售的电池信息
	@Override
	public List<BtySaleInfo> fetchBtySaleInfo(Date date) throws CrudException {
		// TODO Auto-generated method stub
		 Date startDate=date;
		 Date endDate=DateUtils.addDays(startDate, 1);
		 Map<String,Object> map=new HashMap<String,Object>();
		 map.put("startDate", startDate);
		 map.put("endDate", endDate);
		 List<UserBatteryInfo>userBatteryInfos=userBatteryMapper.selectByBuyDate(map);
		 List<BtySaleInfo> btySaleInfos=new ArrayList<BtySaleInfo>();
		 for(UserBatteryInfo userBatteryInfo:userBatteryInfos){
			 BtySaleInfo btySaleInfo=new BtySaleInfo();
			 Battery battery=batteryMapper.selectByPrimaryKey(userBatteryInfo.getBatteryId());
			 User user=userMapper.selectByPrimaryKey(userBatteryInfo.getUserId());
			 User reseller=userMapper.selectByPrimaryKey(battery.getResellerId());
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         String dateString = formatter.format(userBatteryInfo.getBuyDate());
			 
			 btySaleInfo.setBtyImei(battery.getImei());
			 btySaleInfo.setBtySimNo(battery.getSimNo());
			 btySaleInfo.setBtySn(battery.getSn());
			 btySaleInfo.setUserName(user.getUserName());
			 btySaleInfo.setUserphone(user.getMobilePhone());
			 btySaleInfo.setResellerName(reseller.getUserName());
			 btySaleInfo.setResellerPhone(reseller.getMobilePhone());
			 btySaleInfo.setSaleDate(dateString);
			 
			 btySaleInfos.add(btySaleInfo);
		 }
		 
		return btySaleInfos;
	}

}
