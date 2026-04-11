package com.farmfresh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username:}")
	private String mailFrom;

	@Value("${app.frontend-base-url:http://localhost:5173}")
	private String frontendBaseUrl;

	public NotificationService(org.springframework.beans.factory.ObjectProvider<JavaMailSender> mailSenderProvider) {
		this.mailSender = mailSenderProvider.getIfAvailable();
	}

	public void sendPasswordResetEmail(String toEmail, String rawToken) {
		String link = frontendBaseUrl.replaceAll("/$", "") + "/reset-password?token=" + rawToken;
		String body = "Reset your Farm Fresh password using this link (valid 1 hour):\n\n" + link
				+ "\n\nIf you did not request this, ignore this email.";
		if (mailSender == null || mailFrom == null || mailFrom.isBlank()) {
			log.warn("Email not configured. Password reset for {} — use this one-time link: {}", toEmail, link);
			return;
		}
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(mailFrom);
		msg.setTo(toEmail);
		msg.setSubject("Farm Fresh password reset");
		msg.setText(body);
		mailSender.send(msg);
	}

	public void sendPhoneVerificationEmail(String toEmail, String phoneLast4, String code) {
		String body = "Your Farm Fresh phone verification code"
				+ (phoneLast4 != null && !phoneLast4.isBlank() ? " (number ending " + phoneLast4 + ")" : "")
				+ ":\n\n" + code + "\n\nThis code expires in 10 minutes. If you did not request this, ignore this email.";
		if (mailSender == null || mailFrom == null || mailFrom.isBlank()) {
			log.warn("Email not configured. Phone verification for {} — code: {}", toEmail, code);
			return;
		}
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(mailFrom);
		msg.setTo(toEmail);
		msg.setSubject("Farm Fresh phone verification code");
		msg.setText(body);
		mailSender.send(msg);
	}
}
