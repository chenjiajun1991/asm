package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.User;

public interface UserService {

    @Transactional
    public User signup(String mobilePhone, String authCode, String hassPwd, String deviceInfo) throws CrudException;

    @Transactional
    public User signin(String mobilePhone, String hassPwd, String deviceInfo) throws CrudException;

    @Transactional
    public int resetPwd(String mobilePhone, String authCode, String hassPwd, String deviceInfo) throws CrudException;

    @Transactional
    public int updatePwd(String mobilePhone, String authCode, String oldHassPwd, String newHashPwd, String deviceInfo) throws CrudException;

    @Transactional
    public List<BatteryInfo> fetchBtyInfo(String mobilePhone);

    @Transactional
    public User fetchUserByPhone(String mobilePhone);
}
