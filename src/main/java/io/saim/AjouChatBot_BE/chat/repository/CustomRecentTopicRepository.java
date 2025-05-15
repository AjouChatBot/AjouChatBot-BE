package io.saim.AjouChatBot_BE.chat.repository;

import java.util.List;

import io.saim.AjouChatBot_BE.chat.entity.RecentTopic;
import reactor.core.publisher.Flux;

public interface CustomRecentTopicRepository {
	Flux<RecentTopic> search(String query, List<String> keywords, String startDate, String endDate);
}
