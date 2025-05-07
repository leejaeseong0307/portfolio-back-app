package com.recommand.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.recommand.entity.Menu;
import com.recommand.mapper.ContMapper;
import com.recommand.repository.MenuRepository;
import com.recommand.vo.ContVo;

@Service
public class ContService {
	private final ContMapper contMapper;

    public ContService(ContMapper contMapper) {
        this.contMapper = contMapper;
    }

    public List<ContVo> getContents(int offset, int size, String userId, int myChk) {
        return contMapper.getContentsByPage(offset, size, userId, myChk);
    }
    
    public void toggleLike(Long contNo, String userId) {
        if (contMapper.existsLike(contNo, userId)) {
        	contMapper.deleteLike(contNo, userId);
        } else {
        	contMapper.insertLike(contNo, userId);
        }
    }

    public void addLike(Long contNo, String userId) {
    	contMapper.insertLike(contNo, userId);
    }

    public void removeLike(Long contNo, String userId) {
    	contMapper.deleteLike(contNo, userId);
    }
}
