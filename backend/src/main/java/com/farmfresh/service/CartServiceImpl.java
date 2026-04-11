package com.farmfresh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmfresh.custom_exceptions.ResourceNotFoundException;
import com.farmfresh.dto.CartLineResponse;
import com.farmfresh.entities.ShoppingCart;
import com.farmfresh.entities.ShoppingCartLine;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.entities.User;
import com.farmfresh.repository.ShoppingCartRepository;
import com.farmfresh.repository.StockDetailsRepository;

@Service
public class CartServiceImpl implements CartService {

	private final ShoppingCartRepository cartRepository;
	private final StockDetailsRepository stockRepository;

	public CartServiceImpl(ShoppingCartRepository cartRepository, StockDetailsRepository stockRepository) {
		this.cartRepository = cartRepository;
		this.stockRepository = stockRepository;
	}

	@Override
	@Transactional
	public List<CartLineResponse> addOrUpdateItem(User user, int productId, int qty) {
		if (qty <= 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		StockDetails product = stockRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		ShoppingCart cart = cartRepository.findByUser(user).orElseGet(() -> {
			ShoppingCart c = new ShoppingCart();
			c.setUser(user);
			return cartRepository.save(c);
		});

		ShoppingCartLine line = cart.getLines().stream()
				.filter(l -> l.getProduct().getId().equals(productId))
				.findFirst()
				.orElse(null);
		if (line == null) {
			line = new ShoppingCartLine();
			line.setCart(cart);
			line.setProduct(product);
			line.setQuantity(qty);
			cart.getLines().add(line);
		} else {
			line.setQuantity(line.getQuantity() + qty);
		}
		cartRepository.saveAndFlush(cart);
		return toResponses(cartRepository.findById(cart.getId()).orElse(cart));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartLineResponse> getCartLines(User user) {
		return cartRepository.findByUser(user).map(this::toResponses).orElse(new ArrayList<>());
	}

	@Override
	@Transactional
	public void removeLine(User user, long lineId) {
		ShoppingCart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		boolean removed = cart.getLines().removeIf(line -> line.getId() != null && line.getId().equals(lineId));
		if (!removed) {
			throw new ResourceNotFoundException("Cart line not found");
		}
		cartRepository.save(cart);
	}

	@Override
	@Transactional
	public void clearCart(User user) {
		cartRepository.findByUser(user).ifPresent(cart -> {
			cart.getLines().clear();
			cartRepository.save(cart);
		});
	}

	private List<CartLineResponse> toResponses(ShoppingCart cart) {
		List<CartLineResponse> out = new ArrayList<>();
		for (ShoppingCartLine line : cart.getLines()) {
			StockDetails p = line.getProduct();
			double amount = line.getQuantity() * p.getPricePerUnit();
			int farmerId = p.getFarmer1() != null ? p.getFarmer1().getFarmerId() : 0;
			out.add(new CartLineResponse(line.getId(), p.getId(), p.getStockItem(), line.getQuantity(),
					p.getPricePerUnit(), amount, farmerId));
		}
		return out;
	}
}
