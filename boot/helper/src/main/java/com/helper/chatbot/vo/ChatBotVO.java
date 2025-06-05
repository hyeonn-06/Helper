package com.helper.chatbot.vo;

import lombok.Data;

@Data
public class ChatBotVO {
	private String question;
	private String answer;
	private String member_id;
	private String response_message;
}
