package io.saim.AjouChatBot_BE.chat.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.saim.AjouChatBot_BE.chat.dto.SendMessageRequestDTO;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chatbot")
public class ChatController {

	@PostMapping(value = "/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamChat(@RequestBody SendMessageRequestDTO request) {
		String userMessage = request.getMessage();

		//프론트에서 받은 메시지를 그대로 스트리밍 응답으로 반환
		return Flux.fromStream(userMessage.chars().mapToObj(c -> String.valueOf((char) c)))
			.delayElements(Duration.ofMillis(50));
	}
}
