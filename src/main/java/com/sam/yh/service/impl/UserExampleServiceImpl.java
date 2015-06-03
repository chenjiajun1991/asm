package com.sam.yh.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.dao.UserExampleMapper;
import com.sam.yh.model.UserExample;
import com.sam.yh.service.UserExampleService;

/**
 * @author nate
 */

@Service("userExampleService")
public class UserExampleServiceImpl implements UserExampleService {

    @Resource
    private UserExampleMapper userExampleMapper;

    public UserExample getUserById(int userId) {
        return userExampleMapper.selectByPrimaryKey(userId);
    }

}
