package com.sam.yh.dao;

import com.sam.yh.model.UserExample;

public interface UserExampleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserExample record);

    int insertSelective(UserExample record);

    UserExample selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserExample record);

    int updateByPrimaryKey(UserExample record);
}