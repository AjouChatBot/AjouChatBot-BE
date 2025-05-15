package io.saim.AjouChatBot_BE.account.service;

import java.util.HashMap;
import java.util.Map;

import io.saim.AjouChatBot_BE.account.dto.AcademicSettingResponseDTO;
import io.saim.AjouChatBot_BE.account.entity.AccountInfo;
import io.saim.AjouChatBot_BE.account.repository.AcademicSettingRepository;
import io.saim.AjouChatBot_BE.account.repository.AccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountInfoRepository accountInfoRepository;

	public Mono<AccountInfo> getAccountInfo(String userId) {
		return accountInfoRepository.findById(userId);
	}

	private final AcademicSettingRepository academicSettingRepository;

	public Mono<AcademicSettingResponseDTO> getAcademicSettings(String userId) {
		return academicSettingRepository.findById(userId)
			.map(setting -> {
				AcademicSettingResponseDTO dto = new AcademicSettingResponseDTO();
				dto.setAutoCollect(setting.isAutoCollect());
				dto.setUseAcademicInfo(setting.isUseAcademicInfo());

				Map<String, Boolean> categories = new HashMap<>();
				categories.put("enrollment_info", setting.getAllowedCategories().isEnrollmentInfo());
				categories.put("admission_info", setting.getAllowedCategories().isAdmissionInfo());
				categories.put("course_info", setting.getAllowedCategories().isCourseInfo());
				categories.put("grade_info", setting.getAllowedCategories().isGradeInfo());
				categories.put("registration_info", setting.getAllowedCategories().isRegistrationInfo());

				dto.setAllowedCategories(categories);
				return dto;
			});
	}
}
