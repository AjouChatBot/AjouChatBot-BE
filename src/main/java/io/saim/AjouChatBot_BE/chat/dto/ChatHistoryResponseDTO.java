package io.saim.AjouChatBot_BE.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatHistoryResponseDTO {
	private String status = "success";

	private DataContent data;

	@Data
	public static class DataContent {
		private String conversation_id;
		private List<ChatMessageDTO> messages;
	}
}
