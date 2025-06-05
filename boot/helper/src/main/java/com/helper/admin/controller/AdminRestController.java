package com.helper.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helper.notice.service.NoticeService;
import com.helper.notice.vo.NoticeVO;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/admin")
public class AdminRestController {

	@Autowired
	private NoticeService noticeService;
	
	@PostMapping("/notice/write.do")
	public ResponseEntity<String> noticeWrite(@RequestBody NoticeVO vo) {
		log.info("noticeWrite mapping");
		int result = noticeService.write(vo);
		
		if(result == 0)
			return new ResponseEntity<>("공지사항 글 등록 실패", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		return new ResponseEntity<>("공지사항 글 등록 성공", HttpStatus.OK);
	}
	
	@PostMapping("/notice/update.do")
	public ResponseEntity<String> noticeUpdate(@RequestBody NoticeVO vo) {
		log.info("noticeUpdate mapping");
		int result = noticeService.update(vo);
		
		if(result == 0)
			return new ResponseEntity<>("공지사항 글 수정 실패", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		return new ResponseEntity<>("공지사항 글 수정 성공", HttpStatus.OK);
	}
	
	@PostMapping("/notice/delete.do")
	public ResponseEntity<String> noticeDelete(@RequestBody NoticeVO vo) {
		log.info("noticeDelete mapping");
		int result = noticeService.delete(vo);
		
		if(result == 0)
			return new ResponseEntity<>("공지사항 글 삭제 실패", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		return new ResponseEntity<>("공지사항 글 삭제 성공", HttpStatus.OK);
	}
	
	
}
