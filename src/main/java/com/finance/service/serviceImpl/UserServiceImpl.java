package com.finance.service.serviceImpl;

import com.finance.dao.UserDao;
import com.finance.service.UserService;
import com.finance.util.myutil.EncryptUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zt on 2017/2/3.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Override
    public boolean userLogin(String userId, String pwd) {
        pwd = EncryptUtil.encodeMD5(pwd).toUpperCase();
        int result = userDao.validLogin(userId, pwd);
        return result == 1;
    }
}
