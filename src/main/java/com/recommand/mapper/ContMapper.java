package com.recommand.mapper;

import com.recommand.vo.ContVo;
import com.recommand.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContMapper {

    List<ContVo> getContentsByPage(@Param("offset") int offset, @Param("size") int size, @Param("userId") String userId, @Param("myChk") int myChk);

    int insertLike(@Param("contNo") Long contNo, @Param("userId") String userId);

    int deleteLike(@Param("contNo") Long contNo, @Param("userId") String userId);

    boolean existsLike(@Param("contNo") Long contNo, @Param("userId") String userId);

    void insertContent(ContVo vo);

    void insertLongForm(ContVo vo);

    ContVo findById(@Param("id") Long id);

    List<ContVo> selectPagedLongForms(@Param("offset") int offset, @Param("limit") int limit);

    void updateLongForm(ContVo contVo);

    int deleteLongContentById(@Param("longNo") Long longNo);

    void updateLongView(@Param("id") Long id);

}

