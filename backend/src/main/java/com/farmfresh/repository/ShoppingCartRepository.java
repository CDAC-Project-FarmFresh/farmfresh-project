package com.farmfresh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.ShoppingCart;
import com.farmfresh.entities.User;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

	Optional<ShoppingCart> findByUser(User user);

	Optional<ShoppingCart> findByUser_UserId(Integer userId);
}
