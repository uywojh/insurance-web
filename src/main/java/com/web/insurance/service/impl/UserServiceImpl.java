package com.web.insurance.service.impl;

import com.web.insurance.mapper.UserMapper;
import com.web.insurance.po.User;
import com.web.insurance.service.UserService;
import com.web.insurance.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2020/4/4
 * Package com.web.insurance.service
 *
 * @author dsy
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        String pwd = MD5Util.md5(password);
        return userMapper.selectUserNameByNameAndPassWord(username,pwd);
    }
}
