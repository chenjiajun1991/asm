package com.sam.yh.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtysException;
import com.sam.yh.crud.exception.FetchFollowerException;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.Battery;
import com.sam.yh.model.PubBattery;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryKey;
import com.sam.yh.model.UserFollow;
import com.sam.yh.resp.bean.BtyFollower;
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

}
