package com.recommand.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_menu")
@Data
public class Menu {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 	@Column(name = "menu_no")
	    private int menuNo;
	 	
	 	@Column(name = "menu_name", nullable = false)
	    private String menuName;
	 	
	 	@Column(name = "menu_route", nullable = false)
	    private String menuRoute;
	 	
	 	@Column(name = "menu_sort")
	    private int menuSort = 0;
	 	
	 	@Column(name = "menu_active", nullable = false)
	    private String menuActive;
	 	
}
