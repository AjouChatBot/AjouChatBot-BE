package io.saim.AjouChatBot_BE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors()
			.and()
			.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.requestMatchers("/chatbot/**", "/chatbot/academic-files", "/account/info", "/account/info/academic-settings", "/account/info/delete", "/auth/login", "/auth/status", "/auth/logout", "/chat", "/health", "/chat/subject","/chat/keyword","/subject",
				"/keyword","/auth/refresh","/account/info/track","/chatbot/chat-settings").permitAll()
			.anyRequest().authenticated();

		return http.build();
	}
}
