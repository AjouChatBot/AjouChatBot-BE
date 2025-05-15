package io.saim.AjouChatBot_BE.chat.dto;

import java.util.List;
import lombok.Data;

@Data
public class SendMessageRequestDTO {
	private String message; //사용자 질문
	private boolean newTopic;
	private List<String> keywords;
}
