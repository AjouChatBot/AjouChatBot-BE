package io.saim.AjouChatBot_BE.account.dto;

import java.util.Map;

import lombok.Data;

@Data
public class AcademicSettingResponseDTO {
	private boolean autoCollect;
	private boolean useAcademicInfo;
	private Map<String, Boolean> allowedCategories;
}
