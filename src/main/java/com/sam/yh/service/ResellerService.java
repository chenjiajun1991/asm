package com.sam.yh.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.model.Reseller;
import com.sam.yh.req.bean.LogResellerReq;
import com.sam.yh.req.bean.SubmitBtySpecReq;
import com.sam.yh.resp.bean.ResellerInfo;
import com.sam.yh.resp.bean.ResellerUserBtyInfo;

public interface ResellerService {

    @Transactional
    public void submitBtySpec(SubmitBtySpecReq submitBtySpecReq) throws CrudException;

    @Transactional
    public void logReseller(LogResellerReq logResellerReq) throws CrudException;

    @Transactional
    public List<ResellerUserBtyInfo> fetchResellerBtyInfo(String resellerPhone, int start, int size) throws CrudException;

    @Transactional
    public List<ResellerInfo> fetchResellers(String adminPhone, int start, int size) throws CrudException;

    @Transactional
    public int countResellers();

    @Transactional
    public int countSoldBtys(String resellerPhone) throws CrudException;

    @Transactional
    public Reseller fetchReseller(String resellerPhone) throws CrudException;

}
