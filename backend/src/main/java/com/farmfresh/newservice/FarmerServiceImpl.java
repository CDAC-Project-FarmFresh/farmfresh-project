package com.farmfresh.newservice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmfresh.custom_exceptions.ResourceNotFoundException;
import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.repository.FarmerDetailsRepository;
import com.farmfresh.repository.StockDetailsRepository;

@Service
@Transactional
public class FarmerServiceImpl implements FarmerService {

	private final FarmerDetailsRepository fRepo;
	private final StockDetailsRepository sRepo;

	public FarmerServiceImpl(FarmerDetailsRepository fRepo, StockDetailsRepository sRepo) {
		this.fRepo = fRepo;
		this.sRepo = sRepo;
	}

	@Override
	public List<Farmer> getFarmersList() {
		return fRepo.getAllFarmerDetails();
	}

	@Override
	public Farmer getFarmerDetails(Integer id) {
		return fRepo.getFarmerDetailsById(id);
	}

	@Override
	public List<StockDetails> getFarmerStock(Integer farmerid) {
		if (!fRepo.existsById(farmerid)) {
			return List.of();
		}
		return sRepo.findByFarmer1_FarmerId(farmerid);
	}

	@Override
	public StockDetails getProductDetails(Integer farmerid, Integer productid) {
		return sRepo.findByFarmerAndProductId(farmerid, productid)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
	}

	@Override
	public List<StockDetails> getAllProduct() {
		return sRepo.getAllProductDetails();
	}
}
