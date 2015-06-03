package com.sam.yh.dao;

import com.sam.yh.model.UserGroup;
import com.sam.yh.model.UserGroupKey;

public interface UserGroupMapper {
    int deleteByPrimaryKey(UserGroupKey key);

    int insert(UserGroup record);

    int insertSelective(UserGroup record);

    UserGroup selectByPrimaryKey(UserGroupKey key);

    int updateByPrimaryKeySelective(UserGroup record);

    int updateByPrimaryKey(UserGroup record);
}