package com.farmfresh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.User;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
}
