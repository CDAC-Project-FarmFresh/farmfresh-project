package com.farmfresh.newcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmfresh.dto.ApiResponse;
import com.farmfresh.dto.AuthResponse;
import com.farmfresh.dto.CartLineResponse;
import com.farmfresh.dto.PhoneOtpConfirmRequest;
import com.farmfresh.dto.RegisterRequest;
import com.farmfresh.dto.UserProfileUpdateRequest;
import com.farmfresh.dto.UserResponseDto;
import com.farmfresh.entities.OrderDetails;
import com.farmfresh.entities.User;
import com.farmfresh.newservice.UserService;
import com.farmfresh.repository.UserDetailsRepository;
import com.farmfresh.security.AuthUser;
import com.farmfresh.service.AuthService;
import com.farmfresh.service.CartService;
import com.farmfresh.service.PhoneVerificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	private final CartService cartService;
	private final UserService userService;
	private final UserDetailsRepository userRepository;
	private final AuthService authService;
	private final PhoneVerificationService phoneVerificationService;

	public UserController(CartService cartService, UserService userService, UserDetailsRepository userRepository,
			AuthService authService, PhoneVerificationService phoneVerificationService) {
		this.cartService = cartService;
		this.userService = userService;
		this.userRepository = userRepository;
		this.authService = authService;
		this.phoneVerificationService = phoneVerificationService;
	}

	private AuthUser currentUser() {
		return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private User loadUser() {
		return userRepository.findById(currentUser().getUserId())
				.orElseThrow(() -> new IllegalStateException("User not found"));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/addtocart/{productid}")
	public ResponseEntity<List<CartLineResponse>> addToCart(@PathVariable int productid, @RequestParam int qty) {
		List<CartLineResponse> lines = cartService.addOrUpdateItem(loadUser(), productid, qty);
		return ResponseEntity.ok(lines);
	}

	@GetMapping("/cart")
	public ResponseEntity<List<CartLineResponse>> getCart() {
		return ResponseEntity.ok(cartService.getCartLines(loadUser()));
	}

	@GetMapping("/checkout")
	public ResponseEntity<List<CartLineResponse>> checkoutAlias() {
		return ResponseEntity.ok(cartService.getCartLines(loadUser()));
	}

	@DeleteMapping("/cart/lines/{lineId}")
	public ResponseEntity<Void> removeLine(@PathVariable long lineId) {
		cartService.removeLine(loadUser(), lineId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/removefromcart/{lineId}")
	public ResponseEntity<List<CartLineResponse>> removeLineLegacy(@PathVariable long lineId) {
		cartService.removeLine(loadUser(), lineId);
		return ResponseEntity.ok(cartService.getCartLines(loadUser()));
	}

	@PostMapping("/placeorder")
	public ResponseEntity<Void> placeOrder(@RequestParam(defaultValue = "true") boolean paid) {
		userService.placeOrderFromCart(loadUser(), paid);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/orders")
	public ResponseEntity<List<OrderDetails>> orders() {
		List<OrderDetails> orders = userService.getOrder(currentUser().getUserId());
		return ResponseEntity.ok(orders);
	}

	@PostMapping("/orders")
	public ResponseEntity<List<OrderDetails>> ordersLegacy(@RequestParam Integer userId) {
		if (!currentUser().getUserId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok(userService.getOrder(userId));
	}

	@GetMapping("/profile")
	public ResponseEntity<UserResponseDto> profile() {
		User u = loadUser();
		return ResponseEntity.ok(UserResponseDto.fromEntity(u));
	}

	@PutMapping("/profile")
	public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserProfileUpdateRequest req) {
		User u = loadUser();
		String oldPhone = u.getPhoneNo();
		u.setFirstname(req.getFirstname());
		u.setLastname(req.getLastname());
		u.setEmail(req.getEmail().trim().toLowerCase());
		u.setPhoneNo(req.getPhoneNo());
		u.setAddress(req.getAddress());
		if (!normalizePhone(oldPhone).equals(normalizePhone(u.getPhoneNo()))) {
			u.setPhoneVerified(false);
			u.setPhoneOtpHash(null);
			u.setPhoneOtpExpiresAt(null);
		}
		userRepository.save(u);
		return ResponseEntity.ok(UserResponseDto.fromEntity(u));
	}

	@PostMapping("/phone/request-code")
	public ResponseEntity<ApiResponse> requestPhoneCode() {
		phoneVerificationService.requestCode(currentUser().getUserId());
		return ResponseEntity.ok(new ApiResponse(
				"A verification code was sent to your email (check the server log if outbound mail is not configured)."));
	}

	@PostMapping("/phone/confirm")
	public ResponseEntity<UserResponseDto> confirmPhone(@Valid @RequestBody PhoneOtpConfirmRequest req) {
		User u = phoneVerificationService.confirmCode(currentUser().getUserId(), req.getCode());
		return ResponseEntity.ok(UserResponseDto.fromEntity(u));
	}

	private static String normalizePhone(String s) {
		return s == null ? "" : s.trim();
	}
}
