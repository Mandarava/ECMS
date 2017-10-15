package com.finance.controller;

import com.finance.model.dto.Menu;
import com.finance.service.MenuService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zt 2017/10/7 14:12
 */
@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu")
    public String redirect(){
        return "sidebar";
    }

    @RequestMapping("/menu/list")
    @ResponseBody
    public List<Menu> getMenu(@RequestParam(value = "pid", required = false) String pid) {
        List<Menu> menus;
        if (StringUtils.isEmpty(pid)) {
            menus = menuService.getRoot();
        } else {
            menus = menuService.getNextNodes(pid);
        }
        return menus;
    }

}
