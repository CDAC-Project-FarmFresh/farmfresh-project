package com.farmfresh.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.farmfresh.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final SecretKey key;
	private final long accessExpirationMs;
	private final long refreshExpirationMs;

	public JwtTokenProvider(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.access-expiration-ms}") long accessExpirationMs,
			@Value("${app.jwt.refresh-expiration-ms}") long refreshExpirationMs) {
		byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < 32) {
			throw new IllegalArgumentException("JWT secret must be at least 256 bits");
		}
		this.key = Keys.hmacShaKeyFor(bytes);
		this.accessExpirationMs = accessExpirationMs;
		this.refreshExpirationMs = refreshExpirationMs;
	}

	public String createAccessToken(User user) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + accessExpirationMs);
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("uid", user.getUserId())
				.claim("adm", user.isIsadmin())
				.claim("typ", "access")
				.issuedAt(now)
				.expiration(exp)
				.signWith(key)
				.compact();
	}

	public String createRefreshToken(User user) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + refreshExpirationMs);
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("uid", user.getUserId())
				.claim("adm", user.isIsadmin())
				.claim("typ", "refresh")
				.issuedAt(now)
				.expiration(exp)
				.signWith(key)
				.compact();
	}

	public Claims parseClaims(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	public AuthUser toAuthUser(Claims claims) {
		Integer uid = claims.get("uid", Integer.class);
		String email = claims.getSubject();
		Boolean adm = claims.get("adm", Boolean.class);
		return new AuthUser(uid, email, Boolean.TRUE.equals(adm));
	}

	public boolean isRefreshToken(Claims claims) {
		return "refresh".equals(claims.get("typ", String.class));
	}
}
