package com.sam.yh.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sam.yh.common.ConfigUtils;
import com.sam.yh.common.PwdUtils;
import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.msg.SmsSendUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.LoggingResellerException;
import com.sam.yh.crud.exception.NotAdminException;
import com.sam.yh.crud.exception.SubmitBtySpecException;
import com.sam.yh.dao.BatteryInfoMapper;
import com.sam.yh.dao.ResellerMapper;
import com.sam.yh.dao.UserBatteryMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.BatteryStatus;
import com.sam.yh.enums.ResellerStatus;
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

    @Override
    public void submitBtySpec(SubmitBtySpecReq submitBtySpecReq) throws CrudException {
        if (batteryService.fetchBtyByIMEI(submitBtySpecReq.getBtyImei()) != null) {
            throw new SubmitBtySpecException("请检查电池IMEI号");
        }
        //
        User user = userService.fetchUserByPhone(submitBtySpecReq.getUserPhone());
        if (user == null) {
            user = addLockedUserBySys(submitBtySpecReq.getUserName(), submitBtySpecReq.getUserPhone());
            userCodeService.sendSignupAuthCode(submitBtySpecReq.getUserPhone());
        }

        //

        User resellerUser = userService.fetchUserByPhone(submitBtySpecReq.getResellerPhone());
        if (resellerUser == null) {
            throw new SubmitBtySpecException("经销商不存在，请联系客服。");
        }
        Reseller reseller = resellerMapper.selectByPrimaryKey(resellerUser.getUserId());
        if (reseller == null) {
            throw new SubmitBtySpecException("经销商不存在，请联系客服。");
        }

        //
        boolean isCloudBty = true;
        Battery battery = addBattery(submitBtySpecReq.getBtySN(), submitBtySpecReq.getBtyImei(), submitBtySpecReq.getBtySimNo(), isCloudBty,
                reseller.getUserId());

        //
        UserBattery userBattery = new UserBattery();
        userBattery.setBatteryId(battery.getId());
        userBattery.setUserId(user.getUserId());
        userBattery.setBuyDate(new Date());

        userBatteryMapper.insert(userBattery);

        SmsSendUtils.sendBuyInfo(submitBtySpecReq.getUserPhone());

    }

    private User addLockedUserBySys(String userName, String mobilePhone) {
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        String salt = RandomCodeUtils.genSalt();
        String initPwd = RandomCodeUtils.genInitPwd();
        User user = new User();
        user.setUuid(StringUtils.replace(uuid, "-", ""));
        user.setUserName(userName);
        user.setSalt(salt);

        user.setPassword(PwdUtils.genMd5Pwd(mobilePhone, salt, initPwd));
        user.setMobilePhone(mobilePhone);
        user.setLockStatus(true);
        user.setCreateDate(now);

        userMapper.insertSelective(user);

        return user;
    }

    private Battery addBattery(String btySn, String imei, String simNo, boolean isCloudBty, int resellerId) {
        Date now = new Date();
        Battery battery = new Battery();
        battery.setSn(btySn);
        battery.setPubSn(RandomCodeUtils.genBtyPubSn());
        battery.setImei(imei);
        battery.setSimNo(simNo);
        battery.setBtyType(isCloudBty);
        battery.setStatus(BatteryStatus.NORMAL.getStatus());
        battery.setResellerId(resellerId);
        battery.setSaleStatus(true);
        battery.setCreateDate(now);
        battery.setSaleDate(now);

        return batteryService.addBattery(battery);
    }

    @Override
    public void logReseller(LogResellerReq logResellerReq) throws CrudException {
        if (!isAdmin(logResellerReq.getAdminPhone())) {
            throw new LoggingResellerException("不是管理员，无法录入经销商");
        }
        if (userService.fetchUserByPhone(logResellerReq.getResellerPhone()) != null) {
            throw new LoggingResellerException("经销商手机号码已存在");
        }
        Date now = new Date();
        String uuid = UUID.randomUUID().toString();
        String salt = RandomCodeUtils.genSalt();
        String initPwd = RandomCodeUtils.genInitPwd();

        User user = new User();
        user.setUuid(StringUtils.replace(uuid, "-", ""));
        user.setUserName(logResellerReq.getResellerName());
        user.setSalt(RandomCodeUtils.genSalt());

        user.setPassword(PwdUtils.genMd5Pwd(logResellerReq.getResellerPhone(), salt, initPwd));
        user.setMobilePhone(logResellerReq.getResellerPhone());
        user.setLockStatus(false);
        user.setCreateDate(now);

        userMapper.insertSelective(user);

        Reseller reseller = new Reseller();
        reseller.setUserId(user.getUserId());
        reseller.setOfficeAddress(logResellerReq.getResellerAddress());
        reseller.setCityName(logResellerReq.getCityName());
        reseller.setVerifyStatus(ResellerStatus.VERIFIED.getStatus());
        reseller.setVerifyDate(now);

        // TODO
        // reseller.setCityId(cityId);

        resellerMapper.insertSelective(reseller);

        SmsSendUtils.sendLogResellerSuccess(logResellerReq.getResellerPhone(), initPwd);

    }

    @Override
    public List<ResellerBtyInfo> fetchResellerBtyInfo(String resellerPhone, int start, int size) throws CrudException {
        // TODO Auto-generated method stub
        User reseller = userService.fetchUserByPhone(resellerPhone);
        if (reseller == null) {
            throw new CrudException("经销商不存在");
        }
        if (resellerMapper.selectByPrimaryKey(reseller.getUserId()) == null) {
            throw new CrudException("经销商不存在");
        }
        PageHelper.startPage(1, 2);
        return batteryInfoMapper.selectByReseller(reseller.getUserId());
    }

    @Override
    public List<ResellerInfo> fetchResellers(String adminPhone, int start, int size) throws CrudException {
        // TODO Auto-generated method stub
        if (!isAdmin(adminPhone)) {
            throw new NotAdminException("不是管理员，无法查看");
        }
        PageHelper.startPage(start, size);
        return resellerMapper.selectRellers();
    }

    @Override
    public int countResellers() {
        return resellerMapper.countRellers();
    }

    private boolean isAdmin(String userPhone) {
        List<Object> adminPhones = ConfigUtils.getConfig().getList(ConfigUtils.ADMIN_PHONE);
        Set<String> admins = new HashSet<String>();
        for (Object adminPhone : adminPhones) {
            admins.add((String) adminPhone);
        }

        return admins.contains(userPhone);
    }
}
