package io.saim.AjouChatBot_BE.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RestController
@RequestMapping("/test")
public class MongoTestController {

	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping("/db-status")
	public String checkDatabaseConnection() {
		try {
			List<String> collections = mongoTemplate.getDb().listCollectionNames().into(new java.util.ArrayList<>());
			return "MongoDB 연결 성공! 현재 컬렉션 목록: " + collections;
		} catch (Exception e) {
			return "MongoDB 연결 실패: " + e.getMessage();
		}
	}
}
