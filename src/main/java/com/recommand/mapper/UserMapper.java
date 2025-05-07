package com.recommand.mapper;

import com.recommand.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserVo> getUserList();
    
    int getVisitCnt();
    
    void insertUser(UserVo user);
    
    boolean existsByEmail(String email);
    
    String findPasswordById(@Param("no") String no);
    
    void updateUser(
            @Param("no") String no,
            @Param("name") String name,
            @Param("password") String password
        );
    
    void deleteUserById(@Param("userId") String userId);
    
    void updateFindPassword(@Param("userId") String userId, @Param("password") String password);
    
    UserVo findId(UserVo userVo);
}

