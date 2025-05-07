package com.recommand.service;

import com.recommand.entity.User;
import com.recommand.mapper.UserMapper;
import com.recommand.repository.UserRepository;
import com.recommand.vo.UserVo;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
//	@Autowired
//    private UserMapper userMapper;

    public List<UserVo> getUserList() {
        return userMapper.getUserList();
    }
    
    public int getVisitCnt() {
        return userMapper.getVisitCnt();
    }
    
    public void register(UserVo user) {
        if (userMapper.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        userMapper.insertUser(user);
    }
    
    public void register2(UserVo userVo) {
    	
    	if (userRepository.findByUserId(userVo.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
    	
        // 사용자 이름 중복 체크
        if (userRepository.findByUserEmail(userVo.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userVo.getPassword());

        // 유저 객체 생성
        User user = User.builder()
        		.userId(userVo.getId())
                .userName(userVo.getName())
                .userPassword(encodedPassword)
                .userEmail(userVo.getEmail())
                .createdBy(userVo.getId())
                .updatedBy(userVo.getId())
                .build();

        // DB 저장
        userRepository.save(user);
    }
    
    public void updateUser(UserVo userVo) {
        // 현재 비밀번호 확인
        String encodedPassword = userMapper.findPasswordById(userVo.getNo());
        if (!passwordEncoder.matches(userVo.getCurrentPassword(), encodedPassword)) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 암호화 및 업데이트
        String newEncoded = passwordEncoder.encode(userVo.getNewPassword());
        userMapper.updateUser(userVo.getNo(), userVo.getName(), newEncoded);
    }
    
    public void deleteUser(String userId) {
        userMapper.deleteUserById(userId);
    }
    
    public void findPassword(UserVo userVo) {
    	
    	if (!userRepository.findByUserId(userVo.getId()).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 아이디 입니다.");
        }
    	
        // 사용자 이름 중복 체크
        if (!userRepository.findByUserEmail(userVo.getEmail()).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 이메일 입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userVo.getPassword());

        // 유저 객체 생성
        User user = User.builder()
        		.userId(userVo.getId())
                //.userName(userVo.getName())
                .userPassword(encodedPassword)
                .userEmail(userVo.getEmail())
                .createdBy(userVo.getId())
                .updatedBy(userVo.getId())
                .build();

        // DB 저장
        //userRepository.save(user);
        userMapper.updateFindPassword(userVo.getId(), encodedPassword);
    }
    
    public UserVo findId(UserVo userVo) {
        return userMapper.findId(userVo);
    }
}
