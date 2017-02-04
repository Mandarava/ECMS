package com.finance.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zt on 2017/2/3.
 */
@Repository
public interface UserDao {

    int validLogin(@Param("userId") String userId, @Param("pwd") String pwd);
}
