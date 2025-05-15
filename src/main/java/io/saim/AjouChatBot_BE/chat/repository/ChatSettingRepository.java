package io.saim.AjouChatBot_BE.chat.repository;

import io.saim.AjouChatBot_BE.chat.entity.ChatSetting;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ChatSettingRepository extends ReactiveMongoRepository<ChatSetting, String> {
	Mono<ChatSetting> findByUserId(String userId);
}
