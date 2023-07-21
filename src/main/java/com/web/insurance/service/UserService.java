package com.web.insurance.service;

import com.web.insurance.po.User;

/**
 * Created on 2020/4/4
 * Package com.web.insurance.service
 *
 * @author dsy
 */
public interface UserService {
    User checkUser(String username,String password);
}
