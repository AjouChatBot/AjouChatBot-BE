package io.saim.AjouChatBot_BE.account.dto;

import lombok.Data;

@Data
public class AcademicSettingUpdateRequestDTO {
	private boolean auto_collect;
	private boolean use_academic_info;

	private AllowedCategoriesDTO allowed_categories;

	@Data
	public static class AllowedCategoriesDTO {
		private boolean enrollment_info;
		private boolean admission_info;
		private boolean course_info;
		private boolean grade_info;
		private boolean registration_info;
	}
}
