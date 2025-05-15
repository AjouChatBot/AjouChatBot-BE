package io.saim.AjouChatBot_BE.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import io.saim.AjouChatBot_BE.auth.util.GoogleTokenUtil;
import io.saim.AjouChatBot_BE.auth.util.JwtProvider;
import io.saim.AjouChatBot_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;
	private final GoogleTokenUtil googleTokenUtil; // ✅ 인스턴스 주입

	@PostMapping("/login")
	public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> request) {
		String credential = request.get("credential");

		GoogleIdToken.Payload payload = googleTokenUtil.verify(credential);
		if (payload == null) {
			return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("status", "fail", "message", "토큰 검증 실패")));
		}

		//사용자 정보 추출
		String email = payload.getEmail();
		String name = (String) payload.get("name");
		String picture = (String) payload.get("picture");

		//사용자 등록 or 조회 (임시값)
		long userId = 12345;

		//JWT 발급
		String accessToken = jwtProvider.generateAccessToken(email);
		String refreshToken = jwtProvider.generateRefreshToken(email);

		return Mono.just(ResponseEntity.ok(Map.of(
			"status", "success",
			"message", "로그인 성공",
			"data", Map.of(
				"user_id", userId,
				"email", email,
				"name", name,
				"profile_image", picture,
				"access_token", accessToken,
				"refresh_token", refreshToken
			)
		)));
	}
}
