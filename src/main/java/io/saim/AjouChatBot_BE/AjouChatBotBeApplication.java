package io.saim.AjouChatBot_BE;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "io.saim.AjouChatBot_BE")
public class AjouChatBotBeApplication {

	public static void main(String[] args) {
		//.env 파일 로드
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		//환경 변수 설정
		setSystemProperty("SPRING_PORT", dotenv.get("SPRING_PORT"));
		setSystemProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
		setSystemProperty("MONGODB_DATABASE", dotenv.get("MONGODB_DATABASE"));
		setSystemProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		setSystemProperty("AI_SERVER_URL", dotenv.get("AI_SERVER_URL"));

		SpringApplication.run(AjouChatBotBeApplication.class, args);
	}

	private static void setSystemProperty(String key, String value) {
		if (value != null && !value.isEmpty()) {
			System.setProperty(key, value);
		}
	}
}
