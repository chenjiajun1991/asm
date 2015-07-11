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
    public boolean sendTestAuthCode(String mobilePhone) throws CrudException;

    @Transactional
    public UserCode fetchByUserName(String mobilePhone, int type);

    @Transactional
    public boolean verifyAuthCode(String mobilePhone, int type, String authCode) throws CrudException;

    @Transactional
    public boolean sendWarningMsg(String mobilePhone, String btyImei) throws CrudException;

}
