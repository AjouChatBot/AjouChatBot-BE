package io.saim.AjouChatBot_BE.chat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecentTopicResponseDTO {
	private Integer question_id;
	private String question;
	private String created_at;
	private List<String> keywords;
}
