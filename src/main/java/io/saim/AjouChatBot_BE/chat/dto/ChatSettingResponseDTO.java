package io.saim.AjouChatBot_BE.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatSettingResponseDTO {
	private boolean new_topic_question;
	private boolean include_academic_info;
	private boolean allow_response;
}
