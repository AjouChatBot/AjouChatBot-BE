package io.saim.AjouChatBot_BE.account.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_profiles")
@Data
public class AccountInfo {
	@Id
	private String id;
	private String name;
	private String email;
	private String phone;
	private String department;
	private String college;
	private String major;
	private int grade;
	private boolean trackEnabled = true;
}
