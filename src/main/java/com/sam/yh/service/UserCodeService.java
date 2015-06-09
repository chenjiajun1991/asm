package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.UserCode;

public interface UserCodeService {

    @Transactional
    public String genAndSaveUserSalt(String userName);

    @Transactional
    public void sendAndSaveSmsCode(String userName) throws CrudException;

    @Transactional
    public UserCode fetchByUserName(String userName, int type);

}
