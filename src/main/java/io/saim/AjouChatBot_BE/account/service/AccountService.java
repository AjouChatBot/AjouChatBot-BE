package io.saim.AjouChatBot_BE.account.service;

import java.util.HashMap;
import java.util.Map;
import io.saim.AjouChatBot_BE.account.dto.AcademicSettingResponseDTO;
import io.saim.AjouChatBot_BE.account.dto.AcademicSettingUpdateRequestDTO;
import io.saim.AjouChatBot_BE.account.entity.AcademicSetting;
import io.saim.AjouChatBot_BE.account.entity.AccountInfo;
import io.saim.AjouChatBot_BE.account.repository.AcademicSettingRepository;
import io.saim.AjouChatBot_BE.account.repository.AccountInfoRepository;
import io.saim.AjouChatBot_BE.chat.repository.ChatSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountInfoRepository accountInfoRepository;
	private final AcademicSettingRepository academicSettingRepository;
	private final ChatSettingRepository chatSettingRepository;

	//사용자 계정 정보를 조회하고 없을 경우 자동으로 기본 계정 생성
	public Mono<AccountInfo> getAccountInfo(String userId) {
		return accountInfoRepository.findById(userId)
			.switchIfEmpty(createDefaultAccount(userId)); //자동 생성 추가
	}

	//사용자 계정이 존재하지 않을 경우 기본값으로 저장
	public Mono<AccountInfo> createDefaultAccount(String userId) {
		AccountInfo info = new AccountInfo();
		info.setId(userId);
		info.setEmail(userId); //이메일 = ID
		info.setName("이름미지정");
		info.setPhone("010-0000-0000");
		info.setDepartment("미지정");
		info.setCollege("미지정");
		info.setMajor("미지정");
		info.setGrade(1);
		return accountInfoRepository.save(info);
	}

	public Mono<AcademicSettingResponseDTO> getAcademicSettings(String userId) {
		return academicSettingRepository.findById(userId)
			.switchIfEmpty(Mono.just(new AcademicSetting(userId)))
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

	public Mono<Void> updateAcademicSetting(String userId, AcademicSettingUpdateRequestDTO dto) {
		return academicSettingRepository.findById(userId)
			.flatMap(setting -> {
				setting.setAutoCollect(dto.isAuto_collect());
				setting.setUseAcademicInfo(dto.isUse_academic_info());

				//allowedCategories 안전하게 접근 및 설정
				AcademicSetting.AllowedCategories allowed = setting.getAllowedCategories();
				if (allowed == null) {
					allowed = new AcademicSetting.AllowedCategories();
					setting.setAllowedCategories(allowed);
				}

				AcademicSettingUpdateRequestDTO.AllowedCategoriesDTO c = dto.getAllowed_categories();
				allowed.setEnrollmentInfo(c.isEnrollment_info());
				allowed.setAdmissionInfo(c.isAdmission_info());
				allowed.setCourseInfo(c.isCourse_info());
				allowed.setGradeInfo(c.isGrade_info());
				allowed.setRegistrationInfo(c.isRegistration_info());

				return academicSettingRepository.save(setting).then();
			});
	}

	public Mono<Void> deletePersonalizedData(String userId) {
		return academicSettingRepository.deleteById(userId)
			.then(chatSettingRepository.deleteById(userId));
	}

	public Mono<Boolean> getTrackSetting(String userId) {
		return accountInfoRepository.findById(userId)
			.map(AccountInfo::isTrackEnabled)
			.defaultIfEmpty(true);
	}

	public Mono<Void> updateTrackSetting(String userId, boolean trackEnabled) {
		return accountInfoRepository.findById(userId)
			.flatMap(info -> {
				info.setTrackEnabled(trackEnabled);
				return accountInfoRepository.save(info);
			})
			.then();
	}
}
