package com.sam.yh.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.SamConstants;
import com.sam.yh.crud.exception.BtyFollowException;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.PwdResetException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.PubBatteryInfo;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryKey;
import com.sam.yh.model.UserFollow;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.UserBatteryService;
import com.sam.yh.service.UserCodeService;
import com.sam.yh.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserCodeService userCodeService;

    @Resource
    private UserBatteryService userBatteryService;

    @Resource
    BatteryService batteryService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    @Resource
    private UserFollowMapper userFollowMapper;

    @Resource
    private UserBatteryMapper userBatteryMapper;

    @Override
    public User signup(String mobilePhone, String authCode, String hassPwd, String deviceInfo) throws CrudException {

        User user = fetchUserByPhone(mobilePhone);
        if (user != null && !user.getLockStatus()) {
            throw new UserSignupException("手机号码已使用");
        }
        boolean auth = userCodeService.verifyAuthCode(mobilePhone, UserCodeType.SIGNUP_CODE.getType(), authCode);
        if (!auth) {
            throw new UserSignupException("短信验证码错误");
        }
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        String salt = RandomCodeUtils.genSalt();

        if (user == null) {
            user = new User();
            user.setUuid(StringUtils.replace(uuid, "-", ""));
            user.setUserName(mobilePhone);
            user.setSalt(salt);

            user.setPassword(getHashPwd(mobilePhone, salt, hassPwd));
            user.setMobilePhone(mobilePhone);
            user.setLockStatus(false);
            user.setDeviceInfo(deviceInfo);
            user.setCreateDate(now);
            user.setLoginDate(now);

            userMapper.insert(user);
        } else {
            user.setSalt(salt);

            user.setPassword(getHashPwd(mobilePhone, salt, hassPwd));
            user.setLockStatus(false);
            user.setDeviceInfo(deviceInfo);
            user.setLoginDate(now);

            userMapper.updateByPrimaryKeySelective(user);
        }

        return user;
    }

    @Override
    public User signin(String mobilePhone, String hassPwd, String deviceInfo) throws CrudException {

        User user = fetchUserByPhone(mobilePhone);
        if (user == null || user.getLockStatus()) {
            throw new UserSignupException("用户不存在");
        }

        if (!StringUtils.equals(user.getPassword(), getHashPwd(mobilePhone, user.getSalt(), hassPwd))) {
            throw new UserSignupException("用户名或密码错误");
        }

        user.setLoginDate(new Date());
        user.setDeviceInfo(deviceInfo);
        userMapper.updateByPrimaryKeySelective(user);

        return user;
    }

    @Override
    public User resetPwd(String mobilePhone, String authCode, String hassPwd, String deviceInfo) throws CrudException {
        User user = fetchUserByPhone(mobilePhone);
        if (user == null) {
            throw new PwdResetException("未注册的手机号码");
        }
        boolean auth = userCodeService.verifyAuthCode(mobilePhone, UserCodeType.RESETPWD_CODE.getType(), authCode);
        if (!auth) {
            throw new PwdResetException("短信验证码错误");
        }
        Date now = new Date();
        String salt = user.getSalt();
        user.setPassword(getHashPwd(mobilePhone, salt, hassPwd));
        user.setLockStatus(false);
        user.setLoginDate(now);
        user.setDeviceInfo(deviceInfo);

        userMapper.updateByPrimaryKeySelective(user);

        return user;
    }

    @Override
    public int updatePwd(String mobilePhone, String authCode, String oldHassPwd, String newHashPwd, String deviceInfo) throws CrudException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<PubBatteryInfo> fetchSelfBtyInfo(String mobilePhone) {
        User user = fetchUserByPhone(mobilePhone);
        if (user == null) {
            return Collections.emptyList();
        }
        List<UserBattery> btys = userBatteryService.fetchUserBattery(user.getUserId());
        List<PubBatteryInfo> btyInfo = new ArrayList<PubBatteryInfo>();
        for (UserBattery userBattery : btys) {
            BatteryInfo info = batteryInfoMapper.selectByBtyId(userBattery.getBatteryId());
            if (info != null) {
                PubBatteryInfo pubInfo = new PubBatteryInfo(info);
                pubInfo.setBtyPubSn(userBattery.getBtyPubSn());
                pubInfo.setOwnerPhone(user.getMobilePhone());
                btyInfo.add(pubInfo);
            }
        }

        return btyInfo;
    }

    @Override
    public List<PubBatteryInfo> fetchFriendsBtyInfo(String mobilePhone) {
        User user = fetchUserByPhone(mobilePhone);
        if (user == null) {
            return Collections.emptyList();
        }
        List<UserFollow> btys = userBatteryService.fetchUserFollowBty(user.getUserId());
        List<PubBatteryInfo> btyInfo = new ArrayList<PubBatteryInfo>();
        for (UserFollow userFollow : btys) {
            BatteryInfo info = batteryInfoMapper.selectByBtyId(userFollow.getBatteryId());
            if (info != null) {
                UserBattery userBattery = userBatteryService.fetchUserByBtyId(info.getBatteryId());
                User owner = userMapper.selectByPrimaryKey(userBattery.getUserId());
                PubBatteryInfo pubInfo = new PubBatteryInfo(info);
                pubInfo.setBtyPubSn(userFollow.getBtyPubSn());
                pubInfo.setOwnerPhone(owner.getMobilePhone());
                btyInfo.add(pubInfo);
            }
        }

        return btyInfo;
    }

    @Override
    public User fetchUserByPhone(String mobilePhone) {
        return userMapper.selectByPhone(mobilePhone);
    }

    @Override
    public void followBty(String mobilePhone, String btyPubSn, String btyOwnerPhone) throws CrudException {
        User user = fetchUserByPhone(mobilePhone);
        if (user == null) {
            throw new BtyFollowException("用户不存在");
        }

        User btyOwner = fetchUserByPhone(btyOwnerPhone);
        if (btyOwner == null) {
            throw new BtyFollowException("用户不存在");
        }

        Battery battery = batteryService.fetchBtyByPubSn(btyPubSn);
        if (battery == null) {
            throw new BtyFollowException("电池不存在");
        }
        UserBatteryKey key = new UserBatteryKey();
        key.setUserId(btyOwner.getUserId());
        key.setBatteryId(battery.getId());
        UserBattery userBattery = userBatteryMapper.selectByPrimaryKey(key);
        if (userBattery == null) {
            throw new BtyFollowException("好友手机号码与电池序列号不匹配");
        }

        Date now = new Date();

        List<UserFollow> userFollowBtyList = userBatteryService.fetchUserFollowBty(user.getUserId());
        if (userFollowBtyList != null && userFollowBtyList.size() >= SamConstants.MAX_FOLLOW_COUNT) {
            throw new BtyFollowException("您已达到了最大关注数量");
        }
        for (UserFollow userFollow : userFollowBtyList) {
            if (StringUtils.equals(userFollow.getBtyPubSn(), btyPubSn) && userFollow.getFollowStatus()) {
                throw new BtyFollowException("您已关注了该电池");
            } else if (StringUtils.equals(userFollow.getBtyPubSn(), btyPubSn) && !userFollow.getFollowStatus()) {
                userFollow.setFollowStatus(true);
                userFollow.setFollowDate(now);
                userFollowMapper.updateByPrimaryKeySelective(userFollow);
                return;
            }
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(user.getUserId());
        userFollow.setBatteryId(battery.getId());
        userFollow.setFollowStatus(true);
        userFollow.setFollowDate(now);

        userFollowMapper.insert(userFollow);

    }

    @Override
    public void shareBty(String mobilePhone, String btyPubSn, String friendPhone) throws CrudException {
        User owner = fetchUserByPhone(mobilePhone);
        if (owner == null) {
            throw new BtyFollowException("用户不存在");
        }

        User shareUser = fetchUserByPhone(friendPhone);
        if (shareUser == null) {
            throw new BtyFollowException("好友不存在");
        }

        Battery battery = batteryService.fetchBtyByPubSn(btyPubSn);
        if (battery == null) {
            throw new BtyFollowException("电池不存在");
        }
        UserBatteryKey key = new UserBatteryKey();
        key.setUserId(owner.getUserId());
        key.setBatteryId(battery.getId());
        UserBattery userBattery = userBatteryMapper.selectByPrimaryKey(key);
        if (userBattery == null) {
            throw new BtyFollowException("只能共享自己购买的电池");
        }

        Date now = new Date();

        List<UserFollow> userFollowBtyList = userBatteryService.fetchUserFollowBty(shareUser.getUserId());
        if (userFollowBtyList != null && userFollowBtyList.size() >= SamConstants.MAX_FOLLOW_COUNT) {
            throw new BtyFollowException("您好友已达到了最大关注数量");
        }
        for (UserFollow userFollow : userFollowBtyList) {
            if (StringUtils.equals(userFollow.getBtyPubSn(), btyPubSn) && userFollow.getFollowStatus()) {
                throw new BtyFollowException("您已关注了该电池");
            } else if (StringUtils.equals(userFollow.getBtyPubSn(), btyPubSn) && !userFollow.getFollowStatus()) {
                userFollow.setFollowStatus(true);
                userFollow.setFollowDate(now);
                userFollowMapper.updateByPrimaryKeySelective(userFollow);
                return;
            }
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(shareUser.getUserId());
        userFollow.setBatteryId(battery.getId());
        userFollow.setFollowStatus(true);
        userFollow.setFollowDate(now);

        userFollowMapper.insert(userFollow);

    }

    @Override
    public void unfollowBty(String mobilePhone, String btyPubSn) throws CrudException {
        // TODO Auto-generated method stub

    }

    private String getHashPwd(String mobilePhone, String salt, String password) {
        // TODO
        return password;
    }

}
