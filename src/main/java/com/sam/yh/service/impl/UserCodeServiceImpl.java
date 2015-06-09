package com.sam.yh.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.common.RandomCodeUtils;
import com.sam.yh.common.SamConstants;
import com.sam.yh.crud.exception.SmsCodeSendException;
import com.sam.yh.dao.UserCodeMapper;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.model.UserCode;
import com.sam.yh.service.UserCodeService;

@Service
public class UserCodeServiceImpl implements UserCodeService {

    @Resource
    private UserCodeMapper userCodeMapper;

    @Override
    public String genAndSaveUserSalt(String userName, int type) {
        UserCode userCode = fetchByUserName(userName, UserCodeType.USER_SALT.getType());
        String salt = RandomCodeUtils.genSalt();
        Date now = new Date();
        if (userCode == null) {
            userCode = new UserCode();
            userCode.setMobilePhone(userName);
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
    @Override
    public void sendAndSaveSmsCode(String userName, int type) throws SmsCodeSendException {
        UserCode userCode = fetchByUserName(userName, type);

        String smsCode = RandomCodeUtils.genSmsCode();
        Date now = new Date();
        if (userCode == null) {
            userCode = new UserCode();
            userCode.setMobilePhone(userName);
            userCode.setCodeType(type);
            userCode.setDynamicCode(smsCode);
            userCode.setSendTimes(1);
            userCode.setStatus(true);
            userCode.setSendDate(now);
            userCode.setExpiryDate(DateUtils.addMinutes(now, SamConstants.EXPIRY_TIME));

            userCodeMapper.insert(userCode);
            sendSms(userName, smsCode);
            return;
        }

        if (userCode.getSendTimes() >= SamConstants.MXA_SMS_SEND_TIME && DateUtils.isSameDay(now, userCode.getSendDate())) {
            throw new SmsCodeSendException("已经超过最大发送次数");
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
            sendSms(userName, userCode.getDynamicCode());
            return;
        } else {
            // 验证码有效，重新发送相同的验证码
            userCode.setSendTimes(sendTimes);

            userCodeMapper.updateByPrimaryKey(userCode);
            sendSms(userName, userCode.getDynamicCode());
            return;
        }

    }

    @Override
    public UserCode fetchByUserName(String userName, int type) {
        return userCodeMapper.selectByUserNameAndType(userName, type);
    }

    @Override
    public boolean verifyAuthCode(String userName, int type, String authCode) {
        UserCode userCode = fetchByUserName(userName, type);
        Date now = new Date();

        if (userCode != null && userCode.getStatus() && now.before(userCode.getExpiryDate()) && StringUtils.equals(userCode.getDynamicCode(), authCode)) {
            userCode.setStatus(false);
            userCodeMapper.updateByPrimaryKey(userCode);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发送短信
     * 
     * @param userName
     * @param code
     */
    private void sendSms(String userName, String code) {
        // TODO
        System.out.println("Sms context, userName:" + userName + ", code:" + code);
    }

}
