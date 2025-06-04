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
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

	public Mono<String> extractSubject(String text) {
		Map<String, String> requestBody = Map.of("text", text);

		return webClient.post()
			.uri(aiServerUrl + "/subject")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(SubjectResponse.class)
			.map(SubjectResponse::subject); // 추출된 주제만 반환
	}

	private record SubjectResponse(String subject) {}

	public Mono<List<String>> extractKeywords(String text) {
		Map<String, String> requestBody = Map.of("text", text);

		return webClient.post()
			.uri(aiServerUrl + "/keyword")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(KeywordResponse.class)
			.map(KeywordResponse::keywords);
	}

	private record KeywordResponse(List<String> keywords) {}
}
