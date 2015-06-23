package com.sam.yh.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.SamConstants;
import com.sam.yh.common.msg.SmsSendUtils;
import com.sam.yh.crud.exception.AuthCodeSendException;
import com.sam.yh.crud.exception.AuthCodeVerifyException;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.dao.UserCodeMapper;
import com.sam.yh.dao.UserMapper;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.model.User;
import com.sam.yh.model.UserCode;
import com.sam.yh.service.UserCodeService;

@Service
public class UserCodeServiceImpl implements UserCodeService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserCodeMapper userCodeMapper;

    @Override
    public boolean sendSignupAuthCode(String mobilePhone) throws CrudException {
        User user = userMapper.selectByPhone(mobilePhone);
        if (user != null && !user.getLockStatus()) {
            throw new UserSignupException("手机号码已经注册");
        }
        return sendAndSaveSmsCode(mobilePhone, UserCodeType.SIGNUP_CODE.getType());
    }

    @Override
    public boolean sendResetPwdAuthCode(String mobilePhone) throws CrudException {
        User user = userMapper.selectByPhone(mobilePhone);
        if (user == null) {
            throw new UserSignupException("未注册的手机号码");
        }
        return sendAndSaveSmsCode(mobilePhone, UserCodeType.SIGNUP_CODE.getType());
    }

    @Override
    public boolean sendTestAuthCode(String mobilePhone) throws CrudException {
        return sendAndSaveSmsCode(mobilePhone, UserCodeType.TEST_CODE.getType());
    }

    @Override
    public String genAndSaveUserSalt(String mobilePhone, int type) {
        UserCode userCode = fetchByUserName(mobilePhone, UserCodeType.USER_SALT.getType());
        String salt = RandomCodeUtils.genSalt();
        Date now = new Date();
        if (userCode == null) {
            userCode = new UserCode();
            userCode.setMobilePhone(mobilePhone);
            userCode.setCodeType(UserCodeType.USER_SALT.getType());
            userCode.setDynamicCode(salt);
            userCode.setSendTimes(1);
            userCode.setStatus(true);
            userCode.setSendDate(now);
            userCode.setExpiryDate(DateUtils.addMinutes(now, SamConstants.EXPIRY_TIME));

            userCodeMapper.insert(userCode);
        } else {
            userCode.setDynamicCode(salt);
            userCode.setSendTimes(1);
            userCode.setStatus(true);
            userCode.setSendDate(now);
            userCode.setExpiryDate(DateUtils.addMinutes(now, SamConstants.EXPIRY_TIME));

            userCodeMapper.updateByPrimaryKey(userCode);
        }
        return salt;
    }

    /**
     * 短信验证码发送
     */
    private boolean sendAndSaveSmsCode(String mobilePhone, int type) throws AuthCodeSendException {
        UserCode userCode = fetchByUserName(mobilePhone, type);

        String smsCode = RandomCodeUtils.genSmsCode();
        Date now = new Date();
        if (userCode == null) {
            userCode = new UserCode();
            userCode.setMobilePhone(mobilePhone);
            userCode.setCodeType(type);
            userCode.setDynamicCode(smsCode);
            userCode.setSendTimes(1);
            userCode.setStatus(true);
            userCode.setSendDate(now);
            userCode.setExpiryDate(DateUtils.addMinutes(now, SamConstants.EXPIRY_TIME));

            userCodeMapper.insert(userCode);
            return sendSms(mobilePhone, smsCode);
        }

        if (userCode.getSendTimes() >= SamConstants.MXA_SMS_SEND_TIME && DateUtils.isSameDay(now, userCode.getSendDate())) {
            throw new AuthCodeSendException("已经超过最大发送次数");
        }

        int sendTimes = DateUtils.isSameDay(now, userCode.getSendDate()) ? (userCode.getSendTimes() + 1) : 1;
        if (!userCode.getStatus() || now.after(userCode.getExpiryDate())) {
            // 验证码无效
            userCode.setDynamicCode(smsCode);
            userCode.setSendTimes(sendTimes);
            userCode.setStatus(true);
            userCode.setSendDate(now);
            userCode.setExpiryDate(DateUtils.addMinutes(now, SamConstants.EXPIRY_TIME));

            userCodeMapper.updateByPrimaryKey(userCode);
            return sendSms(mobilePhone, userCode.getDynamicCode());
        } else {
            // 验证码有效，重新发送相同的验证码
            userCode.setSendTimes(sendTimes);

            userCodeMapper.updateByPrimaryKey(userCode);
            return sendSms(mobilePhone, userCode.getDynamicCode());
        }

    }

    @Override
    public UserCode fetchByUserName(String mobilePhone, int type) {
        return userCodeMapper.selectByUserNameAndType(mobilePhone, type);
    }

    @Override
    public boolean verifyAuthCode(String mobilePhone, int type, String authCode) throws CrudException {
        UserCode userCode = fetchByUserName(mobilePhone, type);
        Date now = new Date();

        if (userCode != null && userCode.getStatus() && now.before(userCode.getExpiryDate())
                && StringUtils.equals(userCode.getDynamicCode(), authCode)) {
            userCode.setStatus(false);
            userCodeMapper.updateByPrimaryKey(userCode);
            return true;
        } else {
            throw new AuthCodeVerifyException("验证码错误");
        }
    }

    /**
     * 发送短信
     * 
     * @param mobilePhone
     * @param code
     */
    private boolean sendSms(String mobilePhone, String code) {
        return SmsSendUtils.sendAuthCode(mobilePhone, code);
    }

}
