package com.sam.yh.service;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.req.bean.LogResellerReq;
import com.sam.yh.req.bean.SubmitBtySpecReq;

public interface ResellerService {

    @Transactional
    public void submitBtySpec(SubmitBtySpecReq submitBtySpecReq) throws CrudException;

    @Transactional
    public void logReseller(LogResellerReq logResellerReq) throws CrudException;

}
