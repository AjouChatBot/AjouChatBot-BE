package io.saim.AjouChatBot_BE.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "recent_topics")
public class RecentTopic {
	@Id
	private String id;

	private Integer questionId;
	private String question;
	private String createdAt; // ISO-8601 문자열로 저장
}
