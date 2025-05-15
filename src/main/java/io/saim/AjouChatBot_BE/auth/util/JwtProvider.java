package io.saim.AjouChatBot_BE.auth.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private final SecretKey secretKey = Keys.hmacShaKeyFor(
		Base64.getEncoder().encode("your-super-secret-key-value".getBytes())
	);

	public String generateAccessToken(String email) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
			.signWith(secretKey)
			.compact();
	}

	public String generateRefreshToken(String email) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
			.signWith(secretKey)
			.compact();
	}

	public String getEmailFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}
}
