package com.recommand.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recommand.entity.User;
import com.recommand.service.ContService;
import com.recommand.vo.ContVo;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class ContController {
	
	private final ContService contService;

    public ContController(ContService contService) {
        this.contService = contService;
    }
    
    @GetMapping("/contents")
    public List<ContVo> getContents(
    		 @RequestParam(name = "page", defaultValue = "1") int page,
    		 @RequestParam(name = "size", defaultValue = "6") int size,
    		 @RequestParam(name = "myChk", defaultValue = "0") int myChk,
    		HttpSession session) {
    	
    	Object userObj = session.getAttribute("user");
    	
    	String userId = "";
    	if (userObj != null) {
    		User user = (User) userObj;
    		userId = user.getUserId();
    	}
//    	if (user == null) {
//            throw new RuntimeException("로그인이 필요합니다.");
//        }
    	int offset = (page - 1) * size;
    	List<ContVo> contList = contService.getContents(offset, size, userId, myChk);
    	return contList;
        //return menuService.getActiveMenus();
    }
    
    @PostMapping("/contents/{contNo}/like")
    public ResponseEntity<?> addLike(@PathVariable("contNo") Long contNo,
            HttpSession session) {
        
        Object userObj = session.getAttribute("user");
        String userId = "";
    	if (userObj != null) {
    		User user = (User) userObj;
    		userId = user.getUserId();
    	}else {
    		throw new RuntimeException("로그인이 필요합니다.");
    	}
    	
    	contService.addLike(contNo, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/contents/{contNo}/like")
    public ResponseEntity<?> removeLike(@PathVariable("contNo") Long contNo,
                                        HttpSession session) {

    	Object userObj = session.getAttribute("user");
        String userId = "";
    	if (userObj != null) {
    		User user = (User) userObj;
    		userId = user.getUserId();
    	}else {
    		throw new RuntimeException("로그인이 필요합니다.");
    	}
        
        contService.removeLike(contNo, userId);
        return ResponseEntity.ok().build();
    }
}
