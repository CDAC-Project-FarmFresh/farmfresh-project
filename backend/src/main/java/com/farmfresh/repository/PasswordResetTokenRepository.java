package com.farmfresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from PasswordResetToken p where p.user.userId = :userId")
	void deleteByUserUserId(@Param("userId") Integer userId);

	java.util.Optional<PasswordResetToken> findByTokenHash(String tokenHash);
}
