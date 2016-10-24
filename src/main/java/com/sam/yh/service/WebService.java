package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.User;
import com.sam.yh.model.web.BtySaleInfoModel;

public interface WebService {
	    @Transactional
	    public User adminLogin(String account, String hassPwd) throws CrudException;
	    
	    @Transactional
	    public List<BtySaleInfoModel> fetchAllSaleInfo() throws CrudException;
}
