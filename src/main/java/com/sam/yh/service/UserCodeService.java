package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.UserCode;

public interface UserCodeService {

    @Transactional
    public String genAndSaveUserSalt(String mobilePhone, int type);

    @Transactional
    public boolean sendSignupAuthCode(String mobilePhone) throws CrudException;

    @Transactional
    public boolean sendResetPwdAuthCode(String mobilePhone) throws CrudException;

    @Transactional
    public boolean sendTestAuthCode(String mobilePhone, String content) throws CrudException;

    @Transactional
    public UserCode fetchByUserName(String mobilePhone, int type);

    @Transactional
    public boolean verifyAuthCode(String mobilePhone, int type, String authCode) throws CrudException;

    @Transactional
    public boolean sendWarningMsg(String mobilePhone, String btyImei) throws CrudException;
    
    @Transactional
    public boolean sendWarningMsg(String mobilePhone, String btyImei, String voltage,int flag) throws CrudException;

    @Transactional
    public boolean sendMovingMsg(String mobilePhone, String btyImei) throws CrudException;
    
    //增加一个剪断信号线电压突变报警
    @Transactional
    public boolean sendViolentDestroyClient(String mobilePhone,String btyImei) throws CrudException;
    
    @Transactional
    public boolean sendViolentDestroyService(String mobilePhone,String btyImei,String userName,String userPhone) throws CrudException;
}
