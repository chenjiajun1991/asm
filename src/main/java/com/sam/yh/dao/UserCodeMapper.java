package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.UserCode;

public interface UserCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCode record);

    int insertSelective(UserCode record);

    UserCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCode record);

    int updateByPrimaryKey(UserCode record);

    UserCode selectByUserNameAndType(String mobilePhone, int type);
    
    List<UserCode> selectByMobilePhone(String mobilePhone);
}