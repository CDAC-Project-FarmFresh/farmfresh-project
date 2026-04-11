package com.farmfresh.newservice;

import java.util.List;

import com.farmfresh.entities.OrderDetails;
import com.farmfresh.entities.User;

public interface UserService {

	User getUserDetails(Integer userId);

	List<OrderDetails> getOrder(Integer userId);

	void placeOrderFromCart(User user, boolean paymentConfirmed);
}
