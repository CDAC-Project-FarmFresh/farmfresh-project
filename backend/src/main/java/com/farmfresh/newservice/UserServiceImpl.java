package com.farmfresh.newservice;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmfresh.custom_exceptions.ResourceNotFoundException;
import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.OrderDetails;
import com.farmfresh.entities.Orders;
import com.farmfresh.entities.ShoppingCart;
import com.farmfresh.entities.ShoppingCartLine;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.entities.User;
import com.farmfresh.repository.OrdersRepository;
import com.farmfresh.repository.ShoppingCartRepository;
import com.farmfresh.repository.StockDetailsRepository;
import com.farmfresh.repository.UserDetailsRepository;
import com.farmfresh.service.CartService;

@Service
public class UserServiceImpl implements UserService {

	private final UserDetailsRepository uRepo;
	private final ShoppingCartRepository cartRepository;
	private final StockDetailsRepository stockRepository;
	private final OrdersRepository ordersRepository;
	private final CartService cartService;

	@PersistenceContext
	private EntityManager mgr;

	public UserServiceImpl(UserDetailsRepository uRepo, ShoppingCartRepository cartRepository,
			StockDetailsRepository stockRepository, OrdersRepository ordersRepository, CartService cartService) {
		this.uRepo = uRepo;
		this.cartRepository = cartRepository;
		this.stockRepository = stockRepository;
		this.ordersRepository = ordersRepository;
		this.cartService = cartService;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserDetails(Integer userId) {
		return uRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDetails> getOrder(Integer userId) {
		String jpql = "SELECT NEW com.farmfresh.entities.OrderDetails(o.id, o.orderItem, o.quantity, o.amount, o.orders) FROM OrderDetails o "
				+ "JOIN o.orders ord JOIN ord.user u WHERE u.userId =:userid";
		return mgr.createQuery(jpql, OrderDetails.class).setParameter("userid", userId).getResultList();
	}

	@Override
	@Transactional
	public void placeOrderFromCart(User user, boolean paymentConfirmed) {
		ShoppingCart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		if (cart.getLines().isEmpty()) {
			throw new IllegalStateException("Cart is empty");
		}

		for (ShoppingCartLine line : cart.getLines()) {
			StockDetails managed = stockRepository.findById(line.getProduct().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
			if (managed.getQuantity() < line.getQuantity()) {
				throw new IllegalStateException("Insufficient stock for " + managed.getStockItem());
			}
		}

		Orders order = new Orders();
		for (ShoppingCartLine line : cart.getLines()) {
			StockDetails managed = stockRepository.findById(line.getProduct().getId()).orElseThrow();
			managed.setQuantity(managed.getQuantity() - line.getQuantity());
			stockRepository.save(managed);

			OrderDetails details = new OrderDetails();
			details.setAmount(line.getQuantity() * managed.getPricePerUnit());
			details.setOrderItem(managed.getStockItem());
			details.setQuantity(line.getQuantity());
			Farmer f = managed.getFarmer1();
			details.setFarmer(f);
			details.setOrders(order);
			order.getOrderDetails().add(details);
		}

		order.setDeliveryStatus(false);
		order.setPaymentStatus(paymentConfirmed);
		order.setUser(user);

		long millis = System.currentTimeMillis();
		order.setPlaceOrderDate(new java.sql.Date(millis));
		java.sql.Date todaysDate = new java.sql.Date(new java.util.Date().getTime());
		order.setDeliveryDate(addDays(todaysDate, 3));

		ordersRepository.save(order);
		cartService.clearCart(user);
	}

	public Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return new Date(c.getTimeInMillis());
	}
}
