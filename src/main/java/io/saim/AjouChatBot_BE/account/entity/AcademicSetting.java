package io.saim.AjouChatBot_BE.account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "academic_setting")
public class AcademicSetting {

	@Id
	private String id;

	private boolean autoCollect;
	private boolean useAcademicInfo;

	private AllowedCategories allowedCategories;
	
	public AcademicSetting() {
		this.autoCollect = true;
		this.useAcademicInfo = true;
		this.allowedCategories = new AllowedCategories();
	}

	public AcademicSetting(String userId) {
		this.id = userId;
		this.autoCollect = true;
		this.useAcademicInfo = true;
		this.allowedCategories = new AllowedCategories();
	}

	@Data
	public static class AllowedCategories {
		private boolean enrollmentInfo = true;
		private boolean admissionInfo = true;
		private boolean courseInfo = true;
		private boolean gradeInfo = true;
		private boolean registrationInfo = true;
	}
}
