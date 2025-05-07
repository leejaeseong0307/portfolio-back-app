package com.recommand.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.recommand.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
	
	@RequestMapping(value={"/index.jsp"}, method = RequestMethod.GET)
	public String index(HttpServletRequest req, UserVo userVo, Model model) {
		System.out.println("인덱스 페이지 호출!!");
		
	    
		model.addAttribute("title", "hello");
		return "views/index";
	}
	

}
