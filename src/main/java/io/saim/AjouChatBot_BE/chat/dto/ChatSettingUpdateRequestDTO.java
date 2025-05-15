package io.saim.AjouChatBot_BE.chat.dto;

import lombok.Data;

@Data
public class ChatSettingUpdateRequestDTO {
	private boolean new_topic_question;
	private boolean include_academic_info;
	private boolean allow_response;
}
