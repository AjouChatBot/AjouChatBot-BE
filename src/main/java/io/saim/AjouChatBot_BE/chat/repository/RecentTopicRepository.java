package io.saim.AjouChatBot_BE.chat.repository;

import io.saim.AjouChatBot_BE.chat.entity.RecentTopic;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RecentTopicRepository extends ReactiveMongoRepository<RecentTopic, String> {
	Flux<RecentTopic> findAllByOrderByCreatedAtDesc(); // 최근 질문 순 정렬
}
