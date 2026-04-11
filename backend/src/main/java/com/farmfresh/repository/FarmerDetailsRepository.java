package com.farmfresh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.farmfresh.entities.Farmer;

@Repository
public interface FarmerDetailsRepository extends JpaRepository<Farmer, Integer>{
	
	@Query("SELECT NEW com.farmfresh.entities.Farmer(f.farmerId, f.firstname, f.lastname, f.email, f.phoneNo, f.address) FROM Farmer f WHERE f.farmerId=:fid")
    Farmer getFarmerDetailsById(@Param("fid") Integer id);
	
	 @Query("SELECT NEW com.farmfresh.entities.Farmer(f.farmerId, f.firstname, f.lastname, f.email, f.phoneNo, f.address) FROM Farmer f")
	public List<Farmer> getAllFarmerDetails();
}
