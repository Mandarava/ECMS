package com.finance.dao;

import com.finance.model.dto.Menu;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zt 2017/10/7 14:18
 */
@Repository
public interface MenuDAO {

    List<Menu> findRoot();

    List<Menu> findSubNodes(int pid);

}
