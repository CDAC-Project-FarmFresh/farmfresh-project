package com.farmfresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.Category;

@Repository
public interface CategoryDetailsRepository extends JpaRepository<Category, Integer>{
	
	public Category findByCategoryId(Integer catid);
}
