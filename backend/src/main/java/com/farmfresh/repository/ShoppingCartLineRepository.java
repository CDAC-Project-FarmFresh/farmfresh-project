package com.farmfresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.ShoppingCartLine;

@Repository
public interface ShoppingCartLineRepository extends JpaRepository<ShoppingCartLine, Long> {
}
