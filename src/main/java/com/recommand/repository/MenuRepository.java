package com.recommand.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recommand.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	List<Menu> findAllByMenuActiveOrderByMenuSortAsc(String menuActive);
}
