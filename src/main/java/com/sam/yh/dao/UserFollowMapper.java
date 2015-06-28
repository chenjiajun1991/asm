package com.sam.yh.dao;

import java.util.List;

import com.sam.yh.model.PubBattery;
import com.sam.yh.model.UserFollow;
import com.sam.yh.model.UserFollowKey;
import com.sam.yh.resp.bean.BtyFollower;

public interface UserFollowMapper {
    int deleteByPrimaryKey(UserFollowKey key);

    int insert(UserFollow record);

    int insertSelective(UserFollow record);

    UserFollow selectByPrimaryKey(UserFollowKey key);

    int updateByPrimaryKeySelective(UserFollow record);

    int updateByPrimaryKey(UserFollow record);

    List<UserFollow> selectByUserId(int userId);

    List<PubBattery> selectBtysByUserId(Integer userId);

    List<BtyFollower> selectBtyFollowers(Integer batteryId);
}