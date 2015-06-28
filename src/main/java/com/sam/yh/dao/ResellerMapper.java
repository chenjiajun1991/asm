package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.Reseller;
import com.sam.yh.resp.bean.ResellerInfo;

public interface ResellerMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(Reseller record);

    int insertSelective(Reseller record);

    Reseller selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(Reseller record);

    int updateByPrimaryKey(Reseller record);

    List<ResellerInfo> selectRellers();

    int countRellers();
}