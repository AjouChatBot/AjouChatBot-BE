package io.saim.AjouChatBot_BE.account.controller;

import io.saim.AjouChatBot_BE.account.dto.AcademicSettingUpdateRequestDTO;
import io.saim.AjouChatBot_BE.account.entity.AccountInfo;
import io.saim.AjouChatBot_BE.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	@GetMapping("/info")
	public Mono<Map<String, Object>> getAccountInfo() {
		String mockUserId = "user123"; //Authorization 후 교체

		return accountService.getAccountInfo(mockUserId)
			.map(info -> Map.of(
				"status", "success",
				"data", info
			));
	}

	@GetMapping("/info/academic-settings")
	public Mono<Map<String, Object>> getAcademicSettings() {
		String mockUserId = "user123"; //실제로는 토큰에서 추출
		return accountService.getAcademicSettings(mockUserId)
			.map(dto -> Map.of(
				"status", "success",
				"data", dto
			));
	}

	@PatchMapping("/info/academic-settings")
	public Mono<Map<String, String>> updateAcademicSetting(@RequestBody AcademicSettingUpdateRequestDTO dto) {
		String mockUserId = "user123"; //추후 Authorization에서 파싱 예정

		return accountService.updateAcademicSetting(mockUserId, dto)
			.thenReturn(Map.of(
				"status", "success",
				"message", "학적 정보 설정이 성공적으로 변경되었습니다."
			));
	}
}
