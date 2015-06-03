package com.sam.yh.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.dao.UserMapper;
import com.sam.yh.model.User;
import com.sam.yh.service.UserService;

/**
 * @author nate
 */

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
	private UserMapper userMapper;

    public User getUserById(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

}
