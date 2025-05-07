package com.recommand.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer empno;
    private String ename;
    private String job;
    
    private String bro_ip;
    private String bro_id;
    
    private String no;
    private String id;
    private String name;
    private String email;
    private String password;
    
    private String currentPassword;
    private String newPassword;
}