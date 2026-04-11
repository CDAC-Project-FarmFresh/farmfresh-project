package com.farmfresh.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmfresh.entities.User;
import com.farmfresh.repository.UserDetailsRepository;

@Service
public class PhoneVerificationService {

	private static final Duration OTP_TTL = Duration.ofMinutes(10);
	private static final SecureRandom RANDOM = new SecureRandom();

	private final UserDetailsRepository userRepository;
	private final NotificationService notificationService;
	private final String tokenSecret;

	public PhoneVerificationService(UserDetailsRepository userRepository, NotificationService notificationService,
			@Value("${app.jwt.secret}") String tokenSecret) {
		this.userRepository = userRepository;
		this.notificationService = notificationService;
		this.tokenSecret = tokenSecret;
	}

	@Transactional
	public void requestCode(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalStateException("User not found"));
		String phone = user.getPhoneNo();
		if (phone == null || phone.isBlank()) {
			throw new IllegalArgumentException("Add a phone number to your profile before requesting a code.");
		}
		String code = String.format("%06d", RANDOM.nextInt(1_000_000));
		String hash = CryptoHashes.sha256Hex(tokenSecret + "|phone|" + user.getUserId() + "|" + code);
		user.setPhoneOtpHash(hash);
		user.setPhoneOtpExpiresAt(Instant.now().plus(OTP_TTL));
		userRepository.save(user);
		String last4 = phone.length() >= 4 ? phone.substring(phone.length() - 4) : phone;
		notificationService.sendPhoneVerificationEmail(user.getEmail(), last4, code);
	}

	@Transactional
	public User confirmCode(int userId, String code) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalStateException("User not found"));
		if (user.getPhoneOtpHash() == null || user.getPhoneOtpExpiresAt() == null) {
			throw new IllegalArgumentException("Request a verification code first.");
		}
		if (user.getPhoneOtpExpiresAt().isBefore(Instant.now())) {
			throw new IllegalArgumentException("That code has expired. Request a new one.");
		}
		String expected = CryptoHashes.sha256Hex(tokenSecret + "|phone|" + user.getUserId() + "|" + code.trim());
		if (!expected.equals(user.getPhoneOtpHash())) {
			throw new IllegalArgumentException("Invalid verification code.");
		}
		user.setPhoneVerified(true);
		user.setPhoneOtpHash(null);
		user.setPhoneOtpExpiresAt(null);
		return userRepository.save(user);
	}
}
