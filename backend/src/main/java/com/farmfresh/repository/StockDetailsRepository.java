package com.farmfresh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.StockDetails;

@Repository
public interface StockDetailsRepository extends JpaRepository<StockDetails, Integer> {

	StockDetails findByStockItem(String name);

	@Query("SELECT s FROM StockDetails s WHERE s.farmer1.farmerId = :fid AND s.id = :pid")
	Optional<StockDetails> findByFarmerAndProductId(@Param("fid") Integer farmerid, @Param("pid") Integer productid);

	List<StockDetails> findByFarmer1_FarmerId(Integer farmerId);

	@Query("SELECT NEW com.farmfresh.entities.StockDetails(s.id, s.stockItem, s.quantity, s.pricePerUnit, s.category, s.imagePath) FROM StockDetails s")
	List<StockDetails> getAllProductDetails();

	boolean existsById(Integer productid);

	void deleteById(Integer productid);
}
