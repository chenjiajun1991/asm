package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.UserCode;

public interface UserCodeService {

    @Transactional
    public String genAndSaveUserSalt(String userName, int type);

    @Transactional
    public void sendAndSaveSmsCode(String userName, int type) throws CrudException;

    @Transactional
    public UserCode fetchByUserName(String userName, int type);

    @Transactional
    public boolean verifyAuthCode(String userName, int type, String authCode);

}
