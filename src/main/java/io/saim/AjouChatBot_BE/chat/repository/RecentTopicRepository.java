package io.saim.AjouChatBot_BE.chat.repository;

import io.saim.AjouChatBot_BE.chat.entity.RecentTopic;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RecentTopicRepository extends ReactiveMongoRepository<RecentTopic, String>, CustomRecentTopicRepository {
	// RecentTopicRepository.java
	Flux<RecentTopic> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
