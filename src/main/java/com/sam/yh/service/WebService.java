package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.User;
import com.sam.yh.model.web.BatteryLocInfo;
import com.sam.yh.model.web.BtySaleInfoModel;
import com.sam.yh.model.web.CodeInfoModel;

public interface WebService {
	    @Transactional
	    public User adminLogin(String account, String hassPwd) throws CrudException;
	    
	    @Transactional
	    public List<BtySaleInfoModel> fetchAllSaleInfo() throws CrudException;
	    
	    @Transactional
	    public BtySaleInfoModel fetBtyByImei(String imei) throws CrudException;
	    
	    @Transactional
	    public List<BtySaleInfoModel> fetchBtyByPhone(String mobilePhone) throws CrudException;
	    
	    @Transactional
	    public List<BatteryLocInfo> fetchBtyLocInfo(String imei,int count ,int flag) throws CrudException;
	    
	    @Transactional
	    public List<CodeInfoModel> fetchUserCode(String userPhone) throws CrudException;
}
