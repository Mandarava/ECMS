package com.finance.service;

import com.finance.model.dto.Menu;

import java.util.List;

/**
 * Created by zt 2017/10/7 14:11
 */
public interface MenuService {

    List<Menu> getRoot();

    List<Menu> getNextNodes(String pid);
}
