package com.recommand.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.recommand.entity.Menu;
import com.recommand.repository.MenuRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    
    public List<Menu> getActiveMenus(boolean isLogin) {
    	if(isLogin) {
    		return menuRepository.findAllByMenuActiveOrderByMenuSortAsc("Y");
    	}else {
    		return menuRepository.findAllByMenuActiveAndIsLoginOrderByMenuSortAsc("Y", 1);
    	}
    }
}
