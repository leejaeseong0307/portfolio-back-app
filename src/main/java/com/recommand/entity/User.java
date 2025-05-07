package com.recommand.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user",
		indexes = {
				@Index(name = "idx_user_id", columnList = "user_id")
    		}
		)  // 테이블 이름이 'users'로 지정됨
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password",nullable = false)
    private String userPassword;

    @Column(name = "user_email",nullable = false, unique = true)
    private String userEmail;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    
    private LocalDateTime created_dt;
    
    private LocalDateTime updated_dt;

    @PrePersist
    protected void onCreate() {
        this.created_dt = LocalDateTime.now();
        this.updated_dt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updated_dt = LocalDateTime.now();
    }

    // 필요한 경우: 생성일, 권한 등 추가 가능
    // private String role;
    // private LocalDateTime createdAt;
}
