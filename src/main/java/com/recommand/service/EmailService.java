package com.recommand.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, String> authCodeStore = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAuthCode(String email) {
        String code = createRandomCode();
        authCodeStore.put(email, code); // 인증코드 임시 저장
        
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.schedule(() -> authCodeStore.remove(email), 1, TimeUnit.MINUTES);
        
        String latestCode = code; // save to compare later
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                // 삭제 직전에 현재 코드가 같을 때만 삭제
                if (latestCode.equals(authCodeStore.get(email))) {
                    authCodeStore.remove(email);
                    System.out.println("만료된 인증 코드 삭제: " + email);
                }
            }
        }, 1, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증코드: " + code);
        mailSender.send(message);
    }
    
    @PreDestroy
    public void cleanup() {
        scheduler.shutdown();
    }

    public boolean verifyCode(String email, String inputCode) {
        String savedCode = authCodeStore.get(email);
        return savedCode != null && savedCode.equals(inputCode);
    }

    private String createRandomCode() {
        int length = 6;
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // 0~9
        }
        return code.toString();
    }
}
