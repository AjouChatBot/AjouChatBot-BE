package io.saim.AjouChatBot_BE.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

	@Bean
	public MongoClientFactoryBean mongo() {
		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
		String mongoUri = System.getenv("MONGODB_URI");

		if (mongoUri == null || mongoUri.isEmpty()) {
			mongoUri = System.getProperty("MONGODB_URI");
		}

		if (mongoUri == null || mongoUri.isEmpty()) {
			mongoUri = "mongodb://localhost:27017/ajouchatbot";
		}

		System.out.println("🔹 MongoDB URI: " + mongoUri);
		mongo.setConnectionString(new ConnectionString(mongoUri));
		return mongo;
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		String mongoUri = System.getenv("MONGODB_URI");
		String databaseName = System.getenv("MONGODB_DATABASE");

		if (mongoUri == null || mongoUri.isEmpty()) {
			mongoUri = System.getProperty("MONGODB_URI");
		}
		if (databaseName == null || databaseName.isEmpty()) {
			databaseName = System.getProperty("MONGODB_DATABASE");
		}

		if (mongoUri == null || mongoUri.isEmpty()) {
			mongoUri = "mongodb://localhost:27017/ajouchatbot";
		}
		if (databaseName == null || databaseName.isEmpty()) {
			databaseName = "ajouchatbot";
		}

		System.out.println("🔹 MongoDB Database: " + databaseName);
		return new MongoTemplate(MongoClients.create(mongoUri), databaseName);
	}
}
