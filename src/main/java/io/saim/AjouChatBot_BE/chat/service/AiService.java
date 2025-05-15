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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

	@Value("${ai.server.url}")
	private String aiServerUrl;

	private final WebClient webClient = WebClient.create();

	public Flux<String> sendMessageToAi(String userEmail, SendMessageRequestDTO request) {
		Map<String, Object> requestBody = Map.of(
			"user_id", userEmail,
			"question", request.getMessage(),
			"is_new_topic", request.isNewTopic(),
			"keywords", request.getKeywords()
		);

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
