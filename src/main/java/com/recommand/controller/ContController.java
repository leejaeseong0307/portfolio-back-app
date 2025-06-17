package com.recommand.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recommand.Interceptor.LoggerInterceptor;
import com.recommand.entity.User;
import com.recommand.service.ContService;
import com.recommand.vo.ContVo;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ContController {

	private final ContService contService;

	public ContController(ContService contService) {
		this.contService = contService;
	}

	@GetMapping("/contents")
	public List<ContVo> getContents(
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "6") int size,
			@RequestParam(name = "myChk", defaultValue = "0") int myChk,
			HttpSession session) {

		Object userObj = session.getAttribute("user");

		String userId = "";
		if (userObj != null) {
			User user = (User) userObj;
			userId = user.getUserId();
		}
//    	if (user == null) {
//            throw new RuntimeException("로그인이 필요합니다.");
//        }
		int offset = (page - 1) * size;
		List<ContVo> contList = contService.getContents(offset, size, userId, myChk);
		return contList;
		//return menuService.getActiveMenus();
	}

	@PostMapping("/contents/{contNo}/like")
	public ResponseEntity<?> addLike(@PathVariable("contNo") Long contNo,
									 HttpSession session) {

		Object userObj = session.getAttribute("user");
		String userId = "";
		if (userObj != null) {
			User user = (User) userObj;
			userId = user.getUserId();
		}else {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		contService.addLike(contNo, userId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/contents/{contNo}/like")
	public ResponseEntity<?> removeLike(@PathVariable("contNo") Long contNo,
										HttpSession session) {

		Object userObj = session.getAttribute("user");
		String userId = "";
		if (userObj != null) {
			User user = (User) userObj;
			userId = user.getUserId();
		}else {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		contService.removeLike(contNo, userId);
		return ResponseEntity.ok().build();
	}

	@Value("${file.upload-dir2}")
	private String uploadDir;

	@PostMapping("/upload-image")
	public Map<String, String> uploadImage(@RequestParam("image") MultipartFile image, HttpSession session) throws IOException {
		// 저장 경로 (운영환경에 맞게 수정)
		//String uploadDir = "/home/ubuntu/images"; // 또는 /usr/local/... 같은 실제 경로
		String imageUrl = "";
		Object userObj = session.getAttribute("user");
		if (userObj != null) {
			File folder = new File(uploadDir);
			if (!folder.exists()) folder.mkdirs();

			// 파일 이름 중복 방지
			String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path filePath = Paths.get(uploadDir, filename);

			// 실제 파일 저장
			Files.write(filePath, image.getBytes());

			// 클라이언트에게 제공할 URL
			//imageUrl = "http://localhost:8081/uploads/images/" + filename;
			imageUrl = "/uploads/images/" + filename;
		}

		return Map.of("url", imageUrl);
	}

	@PostMapping("/longform")
	public ResponseEntity<Map<String, Object>> registerLongForm(@RequestBody ContVo contVo, HttpSession session) {

		Object userObj = session.getAttribute("user");

		try {
			if (userObj != null) {
				contService.saveLongForm(contVo);
				//return ResponseEntity.ok("등록 성공");
				return ResponseEntity.ok(Map.of("message", "등록 성공", "id", contVo.getLongNo()));
			}else {
				throw new RuntimeException("로그인이 필요합니다.");
			}
		} catch (Exception e) {
			log.error("에러 발생:", e);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "등록 실패"));
		}
	}

	@GetMapping("/longform/{id}")
	public ResponseEntity<?> getLongFormById(@PathVariable("id") Long id) {
		try {
			contService.updateLongView(id);
			ContVo longForm = contService.getLongFormById(id);
			if (longForm != null) {
				return ResponseEntity.ok(longForm);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("콘텐츠를 찾을 수 없습니다.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}

	@GetMapping("/long")
	public List<ContVo> getPagedLongForms(
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "2") int size
	) {
		int offset = (page - 1) * size;
		return contService.getLongForms(offset, size);
	}

	@PutMapping("/longform/{id}")
	public ResponseEntity<Map<String, Object>> updateLongForm(
			@PathVariable("id") Long longNo,
			@RequestBody ContVo contVo, HttpSession session) {

		Object userObj = session.getAttribute("user");

		try {
			if (userObj != null) {
				contVo.setLongNo(longNo); // 경로에서 받은 ID를 VO에 세팅
				contService.updateLongForm(contVo);
				return ResponseEntity.ok(Map.of("message", "수정 성공", "id", longNo));
			}else {
				throw new RuntimeException("로그인이 필요합니다.");
			}
		} catch (Exception e) {
			log.error("수정 중 에러 발생:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "수정 실패"));
		}
	}

	@DeleteMapping("/longdel/{longNo}")
	public ResponseEntity<String> deleteLongContent(@PathVariable("longNo") Long longNo, HttpSession session) {
		log.info("DELETE 요청 들어옴: longNo={}", longNo);

		Object userObj = session.getAttribute("user");
		try {
			if (userObj != null) {
				boolean deleted = contService.deleteLongContentById(longNo);
				if (deleted) {
					return ResponseEntity.ok("삭제 성공");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 콘텐츠를 찾을 수 없습니다.");
				}
			}else {
				throw new RuntimeException("로그인이 필요합니다.");
			}
		} catch (Exception e) {
			log.error("에러 발생:", e);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
		}
	}


}
