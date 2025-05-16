package io.saim.AjouChatBot_BE.auth.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Collections;

@Component
public class GoogleTokenUtil {

	private final String clientId;

	public GoogleTokenUtil(@Value("${google.client-id}") String clientId) {
		this.clientId = clientId;
	}

	public GoogleIdToken.Payload verify(String idTokenString) {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance()
			)
				.setAudience(Collections.singletonList(clientId))
				.build();

			GoogleIdToken idToken = verifier.verify(idTokenString);
			if (idToken != null) {
				return idToken.getPayload();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
