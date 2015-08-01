package com.sam.yh.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.ConfigUtils;
import com.sam.yh.common.PwdUtils;
import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.SamConstants;
import com.sam.yh.crud.exception.BtyFollowException;
import com.sam.yh.crud.exception.BtyLockException;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.PwdResetException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.BatteryMapper;
import com.sam.yh.dao.ResellerMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserFollowMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.BatteryStatus;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.enums.UserType;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.PubBatteryInfo;
import com.sam.yh.model.Reseller;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.model.UserBatteryKey;
import com.sam.yh.model.UserFollow;
import com.sam.yh.model.UserFollowKey;
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
    private BatteryMapper batteryMapper;

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    @Resource
    private UserFollowMapper userFollowMapper;

    @Resource
    private UserBatteryMapper userBatteryMapper;

    @Resource
    private ResellerMapper resellerMapper;

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
            user.setUserType(UserType.NORMAL_USER.getType());
            user.setSalt(salt);

            user.setPassword(PwdUtils.genMd5Pwd(mobilePhone, salt, hassPwd));
            user.setMobilePhone(mobilePhone);
            user.setLockStatus(false);
            user.setDeviceInfo(deviceInfo);
            user.setCreateDate(now);
            user.setLoginDate(now);

            userMapper.insert(user);
        } else {
            user.setSalt(salt);

            user.setUserType(UserType.NORMAL_USER.getType());
            user.setPassword(PwdUtils.genMd5Pwd(mobilePhone, salt, hassPwd));
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

        if (!StringUtils.equals(user.getPassword(), PwdUtils.genMd5Pwd(mobilePhone, user.getSalt(), hassPwd))) {
            throw new UserSignupException("用户名或密码错误");
        }

        if (!user.getUserType().equals(getUserType(mobilePhone))) {
            user.setUserType(getUserType(mobilePhone));
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
        user.setPassword(PwdUtils.genMd5Pwd(mobilePhone, salt, hassPwd));
        user.setLockStatus(false);
        user.setLoginDate(now);
        user.setDeviceInfo(deviceInfo);

        userMapper.updateByPrimaryKeySelective(user);

        return user;
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
                pubInfo.setBytImei(userBattery.getBytImei());
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
                pubInfo.setBytImei(userFollow.getBytImei());
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

        List<UserFollow> userFollowBtyList = userBatteryService.fetchUserFollowBty(user.getUserId());
        if (userFollowBtyList != null && userFollowBtyList.size() >= SamConstants.MAX_FOLLOW_COUNT) {
            throw new BtyFollowException("您已达到了最大关注数量");
        }
        for (UserFollow userFollow : userFollowBtyList) {
            if (StringUtils.equals(userFollow.getBtyPubSn(), btyPubSn) && userFollow.getFollowStatus()) {
                throw new BtyFollowException("您已关注了该电池");
            }
        }

        innerFollow(user, battery);

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

        List<UserFollow> userFollowBtyList = userBatteryService.fetchUserFollowBty(shareUser.getUserId());
        if (userFollowBtyList != null && userFollowBtyList.size() >= SamConstants.MAX_FOLLOW_COUNT) {
            throw new BtyFollowException("您好友已达到了最大关注数量");
        }
        for (UserFollow userFollow : userFollowBtyList) {
            if (StringUtils.equals(userFollow.getBtyPubSn(), btyPubSn) && userFollow.getFollowStatus()) {
                throw new BtyFollowException("您已关注了该电池");
            }
        }

        innerFollow(shareUser, battery);

    }

    private void innerFollow(User follower, Battery battery) {
        Date now = new Date();

        UserFollowKey followKey = new UserFollowKey();
        followKey.setBatteryId(battery.getId());
        followKey.setUserId(follower.getUserId());

        UserFollow userFollowExist = userFollowMapper.selectByPrimaryKey(followKey);
        if (userFollowExist != null) {
            userFollowExist.setFollowStatus(true);
            userFollowExist.setFollowDate(now);

            userFollowMapper.updateByPrimaryKeySelective(userFollowExist);
        } else {
            UserFollow userFollow = new UserFollow();
            userFollow.setUserId(follower.getUserId());
            userFollow.setBatteryId(battery.getId());
            userFollow.setFollowStatus(true);
            userFollow.setFollowDate(now);

            userFollowMapper.insert(userFollow);
        }
    }

    @Override
    public void unfollowBty(String mobilePhone, String btyPubSn) throws CrudException {
        // TODO Auto-generated method stub

    }

    @Override
    public void unshareBty(String mobilePhone, String btyPubSn, String friendPhone) throws CrudException {
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
            throw new BtyFollowException("只能删除自己购买的电池的关注者");
        }

        Date now = new Date();

        UserFollowKey followKey = new UserFollowKey();
        followKey.setBatteryId(battery.getId());
        followKey.setUserId(shareUser.getUserId());

        UserFollow userFollow = userFollowMapper.selectByPrimaryKey(followKey);
        userFollow.setFollowStatus(false);
        userFollow.setFollowDate(now);
        userFollowMapper.updateByPrimaryKeySelective(userFollow);

    }

    @Override
    public String getUserType(String mobilePhone) throws CrudException {
        List<Object> adminPhones = ConfigUtils.getConfig().getList(ConfigUtils.ADMIN_PHONE);
        Set<String> admins = new HashSet<String>();
        for (Object adminPhone : adminPhones) {
            admins.add((String) adminPhone);
        }

        if (admins.contains(mobilePhone)) {
            return UserType.ADMIN.getType();
        }

        User user = userMapper.selectByPhone(mobilePhone);
        if (user == null) {
            return UserType.NORMAL_USER.getType();
        }

        Reseller reseller = resellerMapper.selectByPrimaryKey(user.getUserId());
        if (reseller != null) {
            return UserType.RESELLER.getType();
        } else {
            return UserType.NORMAL_USER.getType();
        }

    }

    @Override
    public void lockBty(String mobilePhone, String btyImei) throws CrudException {
        User owner = fetchUserByPhone(mobilePhone);
        if (owner == null) {
            throw new BtyLockException("用户不存在");
        }

        Battery battery = batteryService.fetchBtyByIMEI(btyImei);
        if (battery == null) {
            throw new BtyLockException("电池不存在");
        }
        UserBatteryKey key = new UserBatteryKey();
        key.setUserId(owner.getUserId());
        key.setBatteryId(battery.getId());
        UserBattery userBattery = userBatteryMapper.selectByPrimaryKey(key);
        if (userBattery == null) {
            throw new BtyLockException("只能锁定自己购买的电池");
        }

        if (BatteryStatus.LOCKED.getStatus().equals(battery.getStatus())) {
            throw new BtyLockException("电池已经锁定，请先解锁");
        }

        BatteryInfo latestInfo = batteryInfoMapper.selectByBtyId(battery.getId());

        battery.setStatus(BatteryStatus.LOCKED.getStatus());
        battery.setLockLongitude(latestInfo.getLongitude());
        battery.setLockLatitude(latestInfo.getLatitude());
        battery.setLockDate(new Date());

        batteryMapper.updateByPrimaryKeySelective(battery);

    }

    @Override
    public void unlockBty(String mobilePhone, String btyImei) throws CrudException {
        User owner = fetchUserByPhone(mobilePhone);
        if (owner == null) {
            throw new BtyLockException("用户不存在");
        }

        Battery battery = batteryService.fetchBtyByIMEI(btyImei);
        if (battery == null) {
            throw new BtyLockException("电池不存在");
        }
        UserBatteryKey key = new UserBatteryKey();
        key.setUserId(owner.getUserId());
        key.setBatteryId(battery.getId());
        UserBattery userBattery = userBatteryMapper.selectByPrimaryKey(key);
        if (userBattery == null) {
            throw new BtyLockException("只能解锁自己购买的电池");
        }

        battery.setStatus(BatteryStatus.NORMAL.getStatus());
        battery.setLockDate(new Date());

        batteryMapper.updateByPrimaryKeySelective(battery);

    }
}
