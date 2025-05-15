package io.saim.AjouChatBot_BE.chat.entity;

import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "recent_topics")
public class RecentTopic {
	@Id
	private String id;
	private String userEmail; //추가함
	private Integer questionId;
	private String question;
	private String createdAt;
	private List<String> keywords;
}
