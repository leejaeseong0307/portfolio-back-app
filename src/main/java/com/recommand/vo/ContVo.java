package com.recommand.vo;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContVo {
	private Long contNo;
    private String userId;
    private String contTitle;
    private String contImg;
    private String contDetail;
    private String contDel;
    private String contAct;
    private Integer contLike;
    private Integer contView;
    private String createdBy;      // 문자열 (작성자 이름 등)
    private String updatedBy;      // 문자열 (수정자 이름 등)
    private LocalDateTime createdDt; // 날짜/시간은 그대로 유지
    private LocalDateTime updatedDt;
    
    private String likeNo;
    private String likeUserId;
    private int liked;
    private int likeCnt;
    
    private Integer longSeason;
    private String longGanre;
    
    private Long longNo;
}