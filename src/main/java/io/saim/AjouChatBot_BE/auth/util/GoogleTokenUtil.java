package io.saim.AjouChatBot_BE.auth.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

@Component
public class GoogleTokenUtil {

	private final String clientId;

	public GoogleTokenUtil() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream(".env")) {
			Properties prop = new Properties();
			prop.load(input);
			this.clientId = prop.getProperty("GOOGLE_CLIENT_ID");
		} catch (Exception e) {
			throw new RuntimeException(".env에서 GOOGLE_CLIENT_ID를 불러올 수 없습니다.", e);
		}
	}
	public GoogleIdToken.Payload verify(String idTokenString) {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				new NetHttpTransport(),
				JacksonFactory.getDefaultInstance())
				.setAudience(Collections.singletonList(clientId))
				.build();

			GoogleIdToken idToken = verifier.verify(idTokenString);
			return (idToken != null) ? idToken.getPayload() : null;

		} catch (Exception e) {
			e.printStackTrace(); //로그 확인용
			return null;
		}
	}
}
