package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.BatteryInfo;

public interface UserService {

    @Transactional
    public int signup(String userName, String authCode, String hassPwd, String deviceInfo) throws CrudException;

    @Transactional
    public int signin(String userName, String randCode, String hassPwd, String deviceInfo) throws CrudException;

    @Transactional
    public int resetPwd(String userName, String authCode, String hassPwd, String deviceInfo) throws CrudException;

    @Transactional
    public int updatePwd(String userName, String authCode, String oldHassPwd, String newHashPwd, String deviceInfo) throws CrudException;

    @Transactional
    public List<BatteryInfo> fetchBtyInfo(String userName);

}
