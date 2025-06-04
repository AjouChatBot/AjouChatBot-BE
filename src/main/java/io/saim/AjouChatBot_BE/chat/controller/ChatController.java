package io.saim.AjouChatBot_BE.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.saim.AjouChatBot_BE.auth.util.JwtProvider;
import io.saim.AjouChatBot_BE.chat.dto.ChatHistoryResponseDTO;
import io.saim.AjouChatBot_BE.chat.dto.ChatSettingUpdateRequestDTO;
import io.saim.AjouChatBot_BE.chat.dto.SendMessageRequestDTO;
import io.saim.AjouChatBot_BE.chat.service.ChatService;
import io.saim.AjouChatBot_BE.chat.service.FileStorageService;
import io.saim.AjouChatBot_BE.chat.service.AiService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot")
public class ChatController {

	private final ChatService chatService;
	private final FileStorageService fileStorageService;
	private final JwtProvider jwtProvider;
	private final AiService aiService;

	@PostMapping(value = "/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamChat(@RequestHeader("Authorization") String authHeader, @RequestBody SendMessageRequestDTO request) {

		String email = extractEmailFromAuthHeader(authHeader);
		String userMessage = request.getMessage();

		return aiService.sendMessageToAi(email, request);
	}

	@GetMapping("/conversations/{conversation_id}")
	public Mono<ChatHistoryResponseDTO> getChatHistory(@RequestHeader("Authorization") String authHeader, @PathVariable("conversation_id") String conversationId) {

		String email = extractEmailFromAuthHeader(authHeader);
		return chatService.getChatHistory(conversationId);
	}

	@GetMapping("/recent-topics")
	public Mono<Map<String, Object>> getRecentTopics(@RequestHeader("Authorization") String authHeader) {
		String email = extractEmailFromAuthHeader(authHeader);

		return chatService.getRecentTopics(email)
			.collectList()
			.map(list -> {
				Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
				response.put("data", list);
				return response;
			});
	}

	@GetMapping("/search")
	public Mono<Map<String, Object>> search(
		@RequestParam(required = false) String query,
		@RequestParam(required = false) List<String> keywords,
		@RequestParam(required = false) String start_date,
		@RequestParam(required = false) String end_date
	) {
		return chatService.search(query, keywords, start_date, end_date)
			.collectList()
			.map(list -> {
				Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
				response.put("data", list);
				return response;
			});
	}

	@GetMapping("/chat-settings")
	public Mono<Map<String, Object>> getChatSettings(@RequestHeader("Authorization") String authHeader) {
		String email = extractEmailFromAuthHeader(authHeader);

		return chatService.getChatSettings(email)
			.map(dto -> Map.of(
				"status", "success",
				"data", dto
			));
	}

	@PatchMapping("/chat-settings")
	public Mono<Map<String, String>> updateChatSettings(@RequestHeader("Authorization") String authHeader, @RequestBody ChatSettingUpdateRequestDTO dto) {
		String email = extractEmailFromAuthHeader(authHeader);

		return chatService.updateChatSettings(email, dto)
			.thenReturn(Map.of(
				"status", "success",
				"message", "채팅 설정이 성공적으로 변경되었습니다."
			));
	}

	@PostMapping(
		value = "/academic-files",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public Mono<Map<String, Object>> uploadAcademicFile(
		@RequestHeader("Authorization") String authHeader,
		@RequestPart("document_type") String documentType,
		@RequestPart("file") MultipartFile file
	) {
		String email = extractEmailFromAuthHeader(authHeader);

		return fileStorageService.storeFile(file, documentType, email)
			.map(fileUrl -> Map.of(
				"status", "success",
				"message", "파일 업로드 완료",
				"data", Map.of("file_url", fileUrl)
			));
	}

	private String extractEmailFromAuthHeader(String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		return jwtProvider.getEmailFromToken(token);
	}

	@PostMapping("/subject")
	public Mono<Map<String, Object>> extractSubject(@RequestBody Map<String, String> requestBody) {
		String text = requestBody.get("text");

		return aiService.extractSubject(text)
			.map(subject -> {
				Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
				response.put("subject", subject);
				return response;
			});
	}

	@PostMapping("/keyword")
	public Mono<Map<String, Object>> extractKeywords(@RequestBody Map<String, String> requestBody) {
		String text = requestBody.get("text");

		return aiService.extractKeywords(text)
			.map(keywords -> Map.of(
				"status", "success",
				"keywords", keywords
			));
	}
}
