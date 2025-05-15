package io.saim.AjouChatBot_BE.chat.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import io.saim.AjouChatBot_BE.chat.entity.ChatMessage;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
	Flux<ChatMessage> findByConversationId(String conversationId);
}
