package io.saim.AjouChatBot_BE.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageDTO {
	private String sender;      // "user" or "bot"
	private String message;
	private String timestamp;   // ISO 8601 string
}
