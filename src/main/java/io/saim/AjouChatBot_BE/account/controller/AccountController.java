package io.saim.AjouChatBot_BE.account.controller;

import io.saim.AjouChatBot_BE.account.dto.AcademicSettingUpdateRequestDTO;
import io.saim.AjouChatBot_BE.account.service.AccountService;
import io.saim.AjouChatBot_BE.auth.util.JwtProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;
	private final JwtProvider jwtProvider;

	@GetMapping("/info")
	public Mono<Map<String, Object>> getAccountInfo(@RequestHeader("Authorization") String authHeader) {
		String userId = extractEmailFromAuthHeader(authHeader);
		return accountService.getAccountInfo(userId)
			.map(info -> Map.of(
				"status", "success",
				"data", info
			));
	}

	@GetMapping("/info/academic-settings")
	public Mono<Map<String, Object>> getAcademicSettings(@RequestHeader("Authorization") String authHeader) {
		String userId = extractEmailFromAuthHeader(authHeader);
		return accountService.getAcademicSettings(userId)
			.map(dto -> Map.of(
				"status", "success",
				"data", dto
			));
	}

	@PatchMapping("/info/academic-settings")
	public Mono<Map<String, String>> updateAcademicSetting(
		@RequestHeader("Authorization") String authHeader,
		@RequestBody AcademicSettingUpdateRequestDTO dto
	) {
		String userId = extractEmailFromAuthHeader(authHeader);
		return accountService.updateAcademicSetting(userId, dto)
			.thenReturn(Map.of(
				"status", "success",
				"message", "학적 정보 설정이 성공적으로 변경되었습니다."
			));
	}

	@DeleteMapping("/info/delete")
	public Mono<Map<String, String>> deletePersonalizedData(@RequestHeader("Authorization") String authHeader) {
		String userId = extractEmailFromAuthHeader(authHeader);
		return accountService.deletePersonalizedData(userId)
			.thenReturn(Map.of(
				"status", "success",
				"message", "맞춤형 데이터가 초기화되었습니다."
			));
	}

	//JWT 토큰에서 email 추출
	private String extractEmailFromAuthHeader(String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		return jwtProvider.getEmailFromToken(token);
	}
}
