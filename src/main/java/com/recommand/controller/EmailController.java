package com.recommand.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recommand.service.EmailService;
import com.recommand.vo.UserVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
	private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

	// 인증코드 전송
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam("email") String email) {
        emailService.sendAuthCode(email);
        return ResponseEntity.ok("인증코드가 이메일로 전송되었습니다.");
    }

    // 인증코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam("email") String email,
                                             @RequestParam("code") String code) {
        boolean result = emailService.verifyCode(email, code);
        if (result) {
            return ResponseEntity.ok("인증 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패");
        }
    }
}
