package com.asm.yh.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.asm.yh.dao.UserMapper;
import com.asm.yh.model.User;
import com.asm.yh.service.UserService;

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
