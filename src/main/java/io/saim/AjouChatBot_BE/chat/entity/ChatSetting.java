package io.saim.AjouChatBot_BE.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chat_settings")
public class ChatSetting {
	@Id
	private String userId;
	private boolean newTopicQuestion;
	private boolean includeAcademicInfo;
	private boolean allowResponse;
}
