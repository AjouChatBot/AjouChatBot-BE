package io.saim.AjouChatBot_BE.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	private final GoogleTokenUtil googleTokenUtil;

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

	@GetMapping("/status")
	public Mono<ResponseEntity<Map<String, Object>>> status(@RequestHeader("Authorization") String token) {
		try {
			String email = jwtProvider.getEmailFromToken(token);

			//실제 서비스에서는 email로 사용자 정보 조회
			Map<String, Object> data = Map.of(
				"user_id", 12345,
				"name", "홍길동",
				"email", email,
				"profile_image", "https://example.com/profile.jpg",
				"department", "사이버보안학과",
				"college", "소프트웨어융합대학",
				"major", "사이버보안전공",
				"grade", 3
			);

			return Mono.just(ResponseEntity.ok(Map.of(
				"status", "success",
				"data", data
			)));
		} catch (Exception e) {
			return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("status", "fail", "message", "유효하지 않은 토큰")));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
		//access token 파싱 및 유효성 확인
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("status", "fail", "message", "Authorization 헤더가 유효하지 않음"));
		}

		String token = authHeader.substring(7); // "Bearer " 제거

		return ResponseEntity.ok(
			Map.of("status", "success", "message", "로그아웃 성공")
		);
	}
}
