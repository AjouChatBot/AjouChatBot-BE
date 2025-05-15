package io.saim.AjouChatBot_BE.chat.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.saim.AjouChatBot_BE.chat.entity.RecentTopic;
import reactor.core.publisher.Flux;

@Repository
public class CustomRecentTopicRepositoryImpl implements CustomRecentTopicRepository {

	private final ReactiveMongoTemplate mongoTemplate;

	public CustomRecentTopicRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Flux<RecentTopic> search(String query, List<String> keywords, String startDate, String endDate) {
		Criteria criteria = new Criteria();
		List<Criteria> andConditions = new ArrayList<>();

		if (query != null && !query.isBlank()) {
			andConditions.add(Criteria.where("question").regex(query, "i"));
		}

		if (keywords != null && !keywords.isEmpty()) {
			andConditions.add(Criteria.where("keywords").in(keywords));
		}

		//빈 문자열 체크 포함한 날짜 필터
		if (startDate != null && !startDate.isBlank() && endDate != null && !endDate.isBlank()) {
			andConditions.add(Criteria.where("createdAt").gte(startDate).lte(endDate));
		} else if (startDate != null && !startDate.isBlank()) {
			andConditions.add(Criteria.where("createdAt").gte(startDate));
		} else if (endDate != null && !endDate.isBlank()) {
			andConditions.add(Criteria.where("createdAt").lte(endDate));
		}

		if (!andConditions.isEmpty()) {
			criteria.andOperator(andConditions.toArray(new Criteria[0]));
		}

		Query queryObj = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "createdAt"));

		return mongoTemplate.find(queryObj, RecentTopic.class);
	}
}
