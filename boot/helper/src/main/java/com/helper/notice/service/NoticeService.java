package com.helper.notice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.helper.notice.mapper.NoticeMapper;
import com.helper.notice.vo.NoticeVO;
import com.helper.page.util.PageObject;

@Service
public class NoticeService {

	@Autowired
	private NoticeMapper mapper;
	
	public List<NoticeVO> list(PageObject pageObject) {
		pageObject.setTotalRow(mapper.getTotalRow(pageObject));
		return mapper.list(pageObject);
	}
	
	public NoticeVO view(Long notice_no) {
		return mapper.view(notice_no);
	}
	
	public int write(NoticeVO vo) {
		return mapper.write(vo);
	}

	public int update(NoticeVO vo) {
		return mapper.update(vo);
	}
	
	public int delete(NoticeVO vo) {
		return mapper.delete(vo);
	}
}
