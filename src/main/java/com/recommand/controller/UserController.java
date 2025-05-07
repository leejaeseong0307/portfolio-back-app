package com.recommand.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.recommand.entity.User;
import com.recommand.repository.UserRepository;
import com.recommand.service.UserService;
import com.recommand.vo.UserVo;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	@Autowired
    private UserService userService;
	
	private final UserRepository userRepository;

	@PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserVo userVo, HttpSession session) {
        try {
            //userService.register(userVo); // mybatis
            userService.register2(userVo); // jpa
            
            Optional<User> optionalUser = userRepository.findByUserId(userVo.getId());
            User user = optionalUser.get();
            session.setAttribute("user", user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입 성공");
            response.put("name", userVo.getName());
            response.put("email", userVo.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
        	Map<String, Object> error = new HashMap<>();
        	error.put("status", "fail");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
        	Map<String, Object> error = new HashMap<>();
        	error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
	
	@PostMapping("/loginProc")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserVo userVo, HttpSession session) {
        
		Map<String, Object> response = new HashMap<>();
		// 1. 이메일로 사용자 찾기
		//Optional<User> optionalUser = userRepository.findByEmail(userVo.getEmail());
		Optional<User> optionalUser = userRepository.findByUserId(userVo.getId());

		User user = null;
        // 2. 사용자 존재 여부 확인
		if (optionalUser.isPresent()) {
		    user = optionalUser.get();
		    // user 사용
		}else {
			response.put("message", "존재하지 않는 아이디입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        if (!passwordEncoder.matches(userVo.getPassword(), user.getUserPassword())) {
        	response.put("message", "비밀번호가 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
		
        session.setAttribute("user", user);
        
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
            );
        
        response.put("message", "로그인 성공");
        response.put("no", user.getUserNo());
        response.put("id", user.getUserId());
        response.put("email", user.getUserEmail());
        response.put("username", user.getUserName());
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            return ResponseEntity.ok(user); // 사용자 정보 반환
        } else {
        	session.invalidate();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }
    }
	
	@PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return ResponseEntity.ok("로그아웃 완료");
    }
	
	@PostMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody UserVo userVo, HttpSession session) {
        //String sessionUserId = (String) session.getAttribute("user");
        
        Object userObj = session.getAttribute("user");
        String userId = "";
    	if (userObj != null) {
    		User user = (User) userObj;
    		userId = user.getUserId();
    	}else {
    		throw new RuntimeException("로그인이 필요합니다.");
    	}
        
        if (userId == null || !userId.equals(userVo.getId())) {
            throw new RuntimeException("인증된 사용자만 변경할 수 있습니다.");
        }

        userService.updateUser(userVo);
        return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
    }
	
	@DeleteMapping("/user/delete")
	public ResponseEntity<String> deleteUser(@RequestBody Map<String, String> body, HttpSession session) {
		Object userObj = session.getAttribute("user");
        String userId = "";
    	if (userObj != null) {
    		User user = (User) userObj;
    		userId = user.getUserId();
    	}else {
    		throw new RuntimeException("로그인이 필요합니다.");
    	}

        if (userId == null || !userId.equals(body.get("id"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 오류: 로그인 정보가 일치하지 않습니다.");
        }

        userService.deleteUser(userId);
        session.invalidate(); // 세션 종료

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
	
	@PostMapping("/findPw")
    public ResponseEntity<Map<String, Object>> findPassword(@RequestBody UserVo userVo) {
        try {
            userService.findPassword(userVo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "비밀번호가 변경 되었습니다.");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
        	Map<String, Object> error = new HashMap<>();
        	error.put("status", "fail");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
        	Map<String, Object> error = new HashMap<>();
        	error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
	
	@PostMapping("/findId")
    public ResponseEntity<Map<String, Object>> findId(@RequestBody UserVo userVo) {
        try {
            userVo = userService.findId(userVo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", userVo.getId());
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
        	Map<String, Object> error = new HashMap<>();
        	error.put("status", "fail");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
        	Map<String, Object> error = new HashMap<>();
        	error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
	
}
