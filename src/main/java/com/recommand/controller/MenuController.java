package com.recommand.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommand.entity.Menu;
import com.recommand.service.MenuService;

@RestController
@RequestMapping("/api")
public class MenuController {
	
	private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    
    @GetMapping("/menu")
    public List<Menu> getMenus() {
    	List<Menu> menuList = menuService.getActiveMenus();
    	return menuList;
        //return menuService.getActiveMenus();
    }
}
