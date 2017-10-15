package com.finance.service.serviceImpl;

import com.finance.dao.MenuDAO;
import com.finance.model.dto.Menu;
import com.finance.service.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zt 2017/10/7 14:11
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDAO menuDAO;

    @Override
    public List<Menu> getRoot() {
        return menuDAO.findRoot();
    }

    @Override
    public List<Menu> getNextNodes(String pid) {
        return menuDAO.findSubNodes(Integer.valueOf(pid));
    }
}
