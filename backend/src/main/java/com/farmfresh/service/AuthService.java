package com.farmfresh.service;

import com.farmfresh.dto.AuthResponse;
import com.farmfresh.dto.LoginRequest;
import com.farmfresh.dto.RefreshTokenRequest;
import com.farmfresh.dto.RegisterRequest;

public interface AuthService {

	AuthResponse login(LoginRequest request);

	AuthResponse register(RegisterRequest request);

	AuthResponse refresh(RefreshTokenRequest request);

	void requestPasswordReset(String email);

	void resetPassword(String rawToken, String newPassword);
}
