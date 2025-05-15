package io.saim.AjouChatBot_BE.chat.service;

import io.saim.AjouChatBot_BE.chat.dto.ChatHistoryResponseDTO;
import io.saim.AjouChatBot_BE.chat.dto.ChatMessageDTO;
import io.saim.AjouChatBot_BE.chat.entity.ChatMessage;
import io.saim.AjouChatBot_BE.chat.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatMessageRepository chatMessageRepository;

	public Mono<ChatHistoryResponseDTO> getChatHistory(String conversationId) {
		return chatMessageRepository.findByConversationId(conversationId)
			.map(msg -> new ChatMessageDTO(msg.getSender(), msg.getMessage(), msg.getTimestamp()))
			.collectList()
			.map(messages -> {
				ChatHistoryResponseDTO.DataContent data = new ChatHistoryResponseDTO.DataContent();
				data.setConversation_id(conversationId);
				data.setMessages(messages);

				ChatHistoryResponseDTO response = new ChatHistoryResponseDTO();
				response.setData(data);
				return response;
			});
	}
}
