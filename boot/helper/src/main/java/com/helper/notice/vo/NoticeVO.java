package com.helper.notice.vo;

import java.util.Date;

import lombok.Data;

@Data
public class NoticeVO {

	private Long notice_no;
	private String title;
	private String content;
	private Date created_at;
	
}
