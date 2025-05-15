package io.saim.AjouChatBot_BE.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data  // ← 필수
@Document(collection = "chat_messages")
public class ChatMessage {
	@Id
	private String id;

	@Field("conversationId")
	private String conversationId;
	private String sender; //"user" or "bot"
	private String message;
	private String timestamp;
}
