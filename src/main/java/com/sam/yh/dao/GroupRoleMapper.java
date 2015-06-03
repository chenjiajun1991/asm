package com.sam.yh.dao;

import com.sam.yh.model.GroupRole;
import com.sam.yh.model.GroupRoleKey;

public interface GroupRoleMapper {
    int deleteByPrimaryKey(GroupRoleKey key);

    int insert(GroupRole record);

    int insertSelective(GroupRole record);

    GroupRole selectByPrimaryKey(GroupRoleKey key);

    int updateByPrimaryKeySelective(GroupRole record);

    int updateByPrimaryKey(GroupRole record);
}