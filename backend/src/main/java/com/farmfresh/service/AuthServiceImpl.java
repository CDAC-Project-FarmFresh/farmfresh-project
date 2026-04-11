package com.farmfresh.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmfresh.dto.AuthResponse;
import com.farmfresh.dto.LoginRequest;
import com.farmfresh.dto.RefreshTokenRequest;
import com.farmfresh.dto.RegisterRequest;
import com.farmfresh.dto.UserResponseDto;
import com.farmfresh.entities.PasswordResetToken;
import com.farmfresh.entities.User;
import com.farmfresh.repository.PasswordResetTokenRepository;
import com.farmfresh.repository.UserDetailsRepository;
import com.farmfresh.security.JwtTokenProvider;

import io.jsonwebtoken.Claims;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Duration RESET_TTL = Duration.ofHours(1);
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private final UserDetailsRepository userRepository;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final NotificationService notificationService;
	private final String tokenSecret;

	public AuthServiceImpl(UserDetailsRepository userRepository,
			PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider, NotificationService notificationService,
			@Value("${app.jwt.secret}") String tokenSecret) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.notificationService = notificationService;
		this.tokenSecret = tokenSecret;
	}

	@Override
	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
		if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
			throw new BadCredentialsException("Invalid credentials");
		}
		return buildAuthResponse(user);
	}

	@Override
	@Transactional
	public AuthResponse register(RegisterRequest request) {
		String email = request.getEmail().trim().toLowerCase();
		if (userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("Email already registered");
		}
		User user = new User();
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		user.setPhoneNo(request.getPhoneNo());
		user.setPhoneVerified(false);
		user.setAddress(request.getAddress());
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setIsadmin(false);
		user = userRepository.save(user);
		return buildAuthResponse(user);
	}

	@Override
	@Transactional(readOnly = true)
	public AuthResponse refresh(RefreshTokenRequest request) {
		Claims claims = jwtTokenProvider.parseClaims(request.getRefreshToken());
		if (!jwtTokenProvider.isRefreshToken(claims)) {
			throw new BadCredentialsException("Invalid refresh token");
		}
		Integer uid = claims.get("uid", Integer.class);
		User user = userRepository.findById(uid).orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
		return buildAuthResponse(user);
	}

	@Override
	@Transactional
	public void requestPasswordReset(String email) {
		if (email == null || email.isBlank()) {
			return;
		}
		String e = email.trim().toLowerCase();
		Optional<User> opt = userRepository.findByEmail(e);
		if (opt.isEmpty()) {
			return;
		}
		User user = opt.get();
		passwordResetTokenRepository.deleteByUserUserId(user.getUserId());
		String raw = randomHex(32);
		PasswordResetToken token = new PasswordResetToken();
		token.setUser(user);
		token.setTokenHash(CryptoHashes.sha256Hex(tokenSecret + "|prt|" + raw));
		token.setExpiresAt(Instant.now().plus(RESET_TTL));
		passwordResetTokenRepository.save(token);
		notificationService.sendPasswordResetEmail(user.getEmail(), raw);
	}

	@Override
	@Transactional
	public void resetPassword(String rawToken, String newPassword) {
		if (rawToken == null || rawToken.isBlank()) {
			throw new IllegalArgumentException("Reset token is required.");
		}
		String hash = CryptoHashes.sha256Hex(tokenSecret + "|prt|" + rawToken.trim());
		PasswordResetToken row = passwordResetTokenRepository.findByTokenHash(hash)
				.orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset link."));
		if (row.getConsumedAt() != null || row.getExpiresAt().isBefore(Instant.now())) {
			throw new IllegalArgumentException("Invalid or expired reset link.");
		}
		User u = row.getUser();
		u.setPasswordHash(passwordEncoder.encode(newPassword));
		userRepository.save(u);
		row.setConsumedAt(Instant.now());
		passwordResetTokenRepository.save(row);
	}

	private static String randomHex(int numBytes) {
		byte[] buf = new byte[numBytes];
		SECURE_RANDOM.nextBytes(buf);
		return HexFormat.of().formatHex(buf);
	}

	private AuthResponse buildAuthResponse(User user) {
		String access = jwtTokenProvider.createAccessToken(user);
		String refresh = jwtTokenProvider.createRefreshToken(user);
		return new AuthResponse(access, refresh, UserResponseDto.fromEntity(user));
	}
}
