package com.farmfresh.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.farmfresh.entities.User;
import com.farmfresh.repository.UserDetailsRepository;

@Component
public class DataBootstrap implements ApplicationRunner {

	private static final String ADMIN_EMAIL = "admin@farmfresh.local";
	private static final String ADMIN_PASSWORD = "Admin#12345";

	private final UserDetailsRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DataBootstrap(UserDetailsRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (userRepository.count() > 0) {
			return;
		}
		User admin = new User();
		admin.setEmail(ADMIN_EMAIL);
		admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
		admin.setFirstname("System");
		admin.setLastname("Admin");
		admin.setPhoneNo("0000000000");
		admin.setAddress("HQ");
		admin.setIsadmin(true);
		userRepository.save(admin);
	}
}
