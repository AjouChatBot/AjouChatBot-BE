package io.saim.AjouChatBot_BE.chat.dto;

import lombok.Data;

@Data
public class SendMessageRequestDTO {
	private int user_id;
	private String message;
}
