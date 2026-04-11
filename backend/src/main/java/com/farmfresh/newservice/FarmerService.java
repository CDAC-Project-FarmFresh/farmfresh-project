package com.farmfresh.newservice;

import java.util.List;

import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.StockDetails;

public interface FarmerService {

	List<Farmer> getFarmersList();

	Farmer getFarmerDetails(Integer id);

	List<StockDetails> getFarmerStock(Integer farmerid);

	StockDetails getProductDetails(Integer farmerid, Integer productid);

	List<StockDetails> getAllProduct();

}
