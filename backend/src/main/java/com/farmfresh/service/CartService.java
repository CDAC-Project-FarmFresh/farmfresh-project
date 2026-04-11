package com.farmfresh.service;

import java.util.List;

import com.farmfresh.dto.CartLineResponse;
import com.farmfresh.entities.User;

public interface CartService {

	List<CartLineResponse> addOrUpdateItem(User user, int productId, int qty);

	List<CartLineResponse> getCartLines(User user);

	void removeLine(User user, long lineId);

	void clearCart(User user);
}
