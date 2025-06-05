package com.helper.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.helper.notice.vo.NoticeVO;
import com.helper.page.util.PageObject;

@Mapper
public interface NoticeMapper {

	public Long getTotalRow(PageObject pageObject);
	
	public List<NoticeVO> list(PageObject pageObject);
	
	public NoticeVO view(Long notice_no);
	
	public int write(NoticeVO vo);
	
	public int update(NoticeVO vo);
	
	public int delete(NoticeVO vo);
	
}
