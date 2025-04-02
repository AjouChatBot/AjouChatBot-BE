package io.saim.AjouChatBot_BE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/chatbot/**")
			.allowedOrigins("http://localhost:5177")
			.allowedMethods("GET", "POST", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}
