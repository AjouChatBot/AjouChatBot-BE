package io.saim.AjouChatBot_BE.chat.service;

import io.saim.AjouChatBot_BE.chat.dto.SendMessageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AiService {

	@Value("${ai.server.url}")
	private String aiServerUrl;

	private final WebClient webClient = WebClient.create();

	public Flux<String> sendMessageToAi(String userEmail, SendMessageRequestDTO request) {

		Objects.requireNonNull(userEmail, "userEmail must not be null");
		Objects.requireNonNull(request.getMessage(), "message must not be null");


		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("user_id", userEmail);
		requestBody.put("question", request.getMessage());
		requestBody.put("is_new_topic", request.isNewTopic());
		requestBody.put("keywords", request.getKeywords() != null ? request.getKeywords() : Collections.emptyList());

		return webClient.post()
			.uri(aiServerUrl + "/chat")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_OCTET_STREAM)
			.bodyValue(requestBody)
			.retrieve()
			.bodyToFlux(DataBuffer.class)
			.map(dataBuffer -> {
				byte[] bytes = new byte[dataBuffer.readableByteCount()];
				dataBuffer.read(bytes);
				DataBufferUtils.release(dataBuffer);
				return new String(bytes, StandardCharsets.UTF_8);
			});
	}
}
