package com.sam.yh.dao;

import com.sam.yh.model.Reseller;

public interface ResellerMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(Reseller record);

    int insertSelective(Reseller record);

    Reseller selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(Reseller record);

    int updateByPrimaryKey(Reseller record);
}