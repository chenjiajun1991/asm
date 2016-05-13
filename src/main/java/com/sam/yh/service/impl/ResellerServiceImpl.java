package com.sam.yh.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sam.yh.common.PwdUtils;
import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.msg.DahantSmsService;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchResellerException;
import com.sam.yh.crud.exception.LoggingResellerException;
import com.sam.yh.crud.exception.NotAdminException;
import com.sam.yh.crud.exception.SubmitBtySpecException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.ResellerMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.BatteryStatus;
import com.sam.yh.enums.ResellerStatus;
import com.sam.yh.enums.UserType;
import com.sam.yh.model.Battery;
import com.sam.yh.model.Reseller;
import com.sam.yh.model.User;
import com.sam.yh.model.UserBattery;
import com.sam.yh.req.bean.LogResellerReq;
import com.sam.yh.req.bean.SubmitBtySpecReq;
import com.sam.yh.resp.bean.ResellerBtyInfo;
import com.sam.yh.resp.bean.ResellerInfo;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.ResellerService;
import com.sam.yh.service.UserCodeService;
import com.sam.yh.service.UserService;
import com.sam.yh.unicom.sim.UnicomM2mService;

@Service
public class ResellerServiceImpl implements ResellerService {

    @Resource
    UserService userService;

    @Resource
    UserCodeService userCodeService;

    @Resource
    BatteryService batteryService;

    @Resource
    UserMapper userMapper;

    @Resource
    UserBatteryMapper userBatteryMapper;

    @Resource
    ResellerMapper resellerMapper;

    @Resource
    BatteryInfoMapper batteryInfoMapper;

    @Resource
    private DahantSmsService defaultUmsSmsService;

    @Resource
    UnicomM2mService unicomM2mService;

    @Override
    public void submitBtySpec(SubmitBtySpecReq submitBtySpecReq) throws CrudException {
        if (batteryService.fetchBtyByIMEI(submitBtySpecReq.getBtyImei()) != null) {
            throw new SubmitBtySpecException("请检查电池IMEI号");
        }
        if (batteryService.fetchBtyBySimNo(submitBtySpecReq.getBtySimNo()) != null) {
            throw new SubmitBtySpecException("请检查电池sim卡号");
        }
        if (batteryService.fetchBtyBySN(submitBtySpecReq.getBtySN()) != null) {
            throw new SubmitBtySpecException("请检查电池序列号");
        }

        //
        String resellerPhone = submitBtySpecReq.getResellerPhone();
        User resellerUser = userService.fetchUserByPhone(resellerPhone);
        if (resellerUser == null || StringUtils.equals(UserType.NORMAL_USER.getType(), resellerUser.getUserType())) {
            throw new SubmitBtySpecException("经销商不存在，请联系客服。");
        }

        Reseller reseller = resellerMapper.selectByPrimaryKey(resellerUser.getUserId());
        if (reseller == null) {
            throw new SubmitBtySpecException("经销商信息未添加");
        }

        String iccid = unicomM2mService.activateSimCard(submitBtySpecReq.getBtySimNo());

        //
        User user = userService.fetchUserByPhone(submitBtySpecReq.getUserPhone());
        if (user == null) {
            user = addLockedUserBySys(submitBtySpecReq.getUserName(), submitBtySpecReq.getUserPhone());
            userCodeService.sendSignupAuthCode(submitBtySpecReq.getUserPhone());
        }

        //
        boolean isCloudBty = true;
        Battery battery = addBattery(submitBtySpecReq.getBtySN(), submitBtySpecReq.getBtyImei(), submitBtySpecReq.getBtySimNo(), iccid, isCloudBty,
                reseller.getUserId(), reseller.getCityId(), submitBtySpecReq.getBtyQuantity());

        //
        UserBattery userBattery = new UserBattery();
        userBattery.setBatteryId(battery.getId());
        userBattery.setUserId(user.getUserId());
        userBattery.setBuyDate(new Date());

        userBatteryMapper.insert(userBattery);

        defaultUmsSmsService.sendBuyInfo(submitBtySpecReq.getUserPhone());

    }

    private User addLockedUserBySys(String userName, String mobilePhone) {
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        String salt = RandomCodeUtils.genSalt();
        String initPwd = RandomCodeUtils.genInitPwd();
        User user = new User();
        user.setUuid(StringUtils.replace(uuid, "-", ""));
        user.setUserName(userName);
        user.setUserType(UserType.NORMAL_USER.getType());
        user.setSalt(salt);

        user.setPassword(PwdUtils.genMd5Pwd(mobilePhone, salt, initPwd));
        user.setMobilePhone(mobilePhone);
        user.setLockStatus(true);
        user.setCreateDate(now);

        userMapper.insertSelective(user);

        return user;
    }

    private Battery addBattery(String btySn, String imei, String simNo, String iccid, boolean isCloudBty, int resellerId, int cityId, String btyQuantity) {
        Date now = new Date();
        Battery battery = new Battery();
        battery.setSn(btySn);
        battery.setPubSn(RandomCodeUtils.genBtyPubSn());
        battery.setImei(imei);
        battery.setIccid(iccid);
        battery.setSimNo(simNo);
        battery.setBtyType(isCloudBty);
        battery.setStatus(BatteryStatus.NORMAL.getStatus());
        battery.setResellerId(resellerId);
        battery.setCityId(cityId);
        battery.setSaleStatus(true);
        battery.setCreateDate(now);
        battery.setSaleDate(now);
        battery.setBtyQuantity(btyQuantity == null ? 4 : Integer.valueOf(btyQuantity));

        return batteryService.addBattery(battery);
    }

    @Override
    public void logReseller(LogResellerReq logResellerReq) throws CrudException {
        if (!StringUtils.equals(UserType.ADMIN.getType(), userService.getUserType(logResellerReq.getAdminPhone()))) {
            throw new LoggingResellerException("不是管理员，无法录入经销商");
        }

        String resellerPhone = logResellerReq.getResellerPhone();
        User user = userService.fetchUserByPhone(resellerPhone);

        if (user != null) {
            String userType = userService.getUserType(resellerPhone);

            if (StringUtils.equals(UserType.ADMIN.getType(), userType)) {
                throw new LoggingResellerException("您无权添加管理员");
            } else if (StringUtils.equals(UserType.RESELLER.getType(), userType)) {
                throw new LoggingResellerException("经销商手机号码已存在");
            } else if (StringUtils.equals(UserType.NORMAL_USER.getType(), userType)) {
                user.setUserType(UserType.RESELLER.getType());

                userMapper.updateByPrimaryKeySelective(user);

                createReseller(user.getUserId(), logResellerReq);
                defaultUmsSmsService.sendLogResellerSuccess(resellerPhone);
            }
        } else {
            createUser(logResellerReq);
        }

    }

    private void createUser(LogResellerReq logResellerReq) {
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        String salt = RandomCodeUtils.genSalt();
        String initPwd = RandomCodeUtils.genInitPwd();

        User user = new User();
        user.setUuid(StringUtils.replace(uuid, "-", ""));
        user.setUserName(logResellerReq.getResellerName());
        user.setUserType(UserType.RESELLER.getType());
        user.setSalt(salt);

        user.setPassword(PwdUtils.genMd5Pwd(logResellerReq.getResellerPhone(), salt, initPwd));
        user.setMobilePhone(logResellerReq.getResellerPhone());
        user.setLockStatus(false);
        user.setCreateDate(now);

        userMapper.insertSelective(user);

        createReseller(user.getUserId(), logResellerReq);

        defaultUmsSmsService.sendLogResellerSuccess(logResellerReq.getResellerPhone(), initPwd);
    }

    private void createReseller(int userId, LogResellerReq logResellerReq) {
        Date now = new Date();

        Reseller reseller = new Reseller();
        reseller.setUserId(userId);
        reseller.setOfficeAddress(logResellerReq.getResellerAddress());
        reseller.setLongitude(logResellerReq.getLongitude());
        reseller.setLatitude(logResellerReq.getLatitude());
        reseller.setProvinceName(logResellerReq.getProvinceName());
        // TODO
        reseller.setProvinceId(logResellerReq.getProvinceId());
        reseller.setCityName(logResellerReq.getCityName());
        // TODO
        reseller.setCityId(logResellerReq.getCityId());
        reseller.setVerifyStatus(ResellerStatus.VERIFIED.getStatus());
        reseller.setVerifyDate(now);

        resellerMapper.insert(reseller);
    }

    @Override
    public List<ResellerBtyInfo> fetchResellerBtyInfo(String resellerPhone, int start, int size) throws CrudException {
        // TODO Auto-generated method stub
        User reseller = userService.fetchUserByPhone(resellerPhone);

        if (UserType.NORMAL_USER.getType().equals(reseller.getUserType())) {
            throw new CrudException("经销商不存在");
        }
        PageHelper.startPage(start, size);
        return batteryInfoMapper.selectByReseller(reseller.getUserId());
    }

    @Override
    public List<ResellerInfo> fetchResellers(String adminPhone, int start, int size) throws CrudException {

        if (!StringUtils.equals(UserType.ADMIN.getType(), userService.getUserType(adminPhone))) {
            throw new NotAdminException("不是管理员，无法查看");
        }
        PageHelper.startPage(start, size);
        return resellerMapper.selectRellers();
    }

    @Override
    public int countResellers() {
        return resellerMapper.countRellers();
    }

    @Override
    public int countSoldBtys(String resellerPhone) throws CrudException {
        User reseller = userService.fetchUserByPhone(resellerPhone);
        if (reseller == null) {
            throw new CrudException("经销商不存在");
        }
        if (resellerMapper.selectByPrimaryKey(reseller.getUserId()) == null) {
            throw new CrudException("经销商不存在");
        }

        return batteryService.countSoldBtys(reseller.getUserId());
    }

    @Override
    public Reseller fetchReseller(String resellerPhone) throws CrudException {
        User reseller = userService.fetchUserByPhone(resellerPhone);

        if (UserType.NORMAL_USER.getType().equals(reseller.getUserType())) {
            throw new FetchResellerException("经销商不存在");
        }

        Reseller res = resellerMapper.selectByPrimaryKey(reseller.getUserId());

        if (res == null) {
            throw new FetchResellerException("经销商不存在");
        }

        return res;
    }

}
