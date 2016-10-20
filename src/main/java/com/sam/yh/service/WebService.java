package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.User;

public interface WebService {
	 @Transactional
	    public User adminLogin(String account, String hassPwd) throws CrudException;
}
