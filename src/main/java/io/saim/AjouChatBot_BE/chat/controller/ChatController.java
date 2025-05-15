package io.saim.AjouChatBot_BE.chat.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.saim.AjouChatBot_BE.chat.dto.ChatHistoryResponseDTO;
import io.saim.AjouChatBot_BE.chat.dto.ChatMessageDTO;
import io.saim.AjouChatBot_BE.chat.dto.SendMessageRequestDTO;
import io.saim.AjouChatBot_BE.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot")
public class ChatController {

	private final ChatService chatService;

	@PostMapping(value = "/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamChat(@RequestBody SendMessageRequestDTO request) {
		String userMessage = request.getMessage();

		//프론트에서 받은 메시지를 그대로 스트리밍 응답으로 반환
		return Flux.fromStream(userMessage.chars().mapToObj(c -> String.valueOf((char) c)))
			.delayElements(Duration.ofMillis(50));
	}

	@GetMapping("/conversations/{conversation_id}")
	public Mono<ChatHistoryResponseDTO> getChatHistory(@PathVariable("conversation_id") String conversationId) {
		return chatService.getChatHistory(conversationId);
	}

	@GetMapping("/recent-topics")
	public Mono<Map<String, Object>> getRecentTopics() {
		return chatService.getRecentTopics()
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
	public Mono<Map<String, Object>> getChatSettings() {
		// 나중에 Authorization에서 userId 파싱하면 교체
		String mockUserId = "user123";

		return chatService.getChatSettings(mockUserId)
			.map(dto -> Map.of(
				"status", "success",
				"data", dto
			));
	}
}
