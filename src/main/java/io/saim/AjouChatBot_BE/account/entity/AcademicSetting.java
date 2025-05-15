package io.saim.AjouChatBot_BE.account.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "academic_settings")
public class AcademicSetting {

	@Id
	private String userId;

	private boolean autoCollect;
	private boolean useAcademicInfo;

	private AllowedCategories allowedCategories;

	@Data
	public static class AllowedCategories {
		private boolean enrollmentInfo;
		private boolean admissionInfo;
		private boolean courseInfo;
		private boolean gradeInfo;
		private boolean registrationInfo;
	}
}
