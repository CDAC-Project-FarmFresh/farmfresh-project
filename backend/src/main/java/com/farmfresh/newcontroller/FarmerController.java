package com.farmfresh.newcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.newservice.FarmerService;

@RestController
@RequestMapping("/farmer")
public class FarmerController {

	private final FarmerService fService;

	public FarmerController(FarmerService fService) {
		this.fService = fService;
	}

	@GetMapping("/list")
	public ResponseEntity<List<Farmer>> farmersList() {
		return ResponseEntity.ok(fService.getFarmersList());
	}

	@GetMapping("/farmerdetails/{farmerid}")
	public ResponseEntity<Farmer> getFarmerDetails(@PathVariable int farmerid) {
		return ResponseEntity.ok(fService.getFarmerDetails(farmerid));
	}

	@GetMapping("/products/{farmerid}")
	public ResponseEntity<List<StockDetails>> stockDetails(@PathVariable int farmerid) {
		List<StockDetails> products = fService.getFarmerStock(farmerid);
		return ResponseEntity.ok(products != null ? products : List.of());
	}

	@GetMapping("/products/{farmerid}/{productid}")
	public ResponseEntity<StockDetails> productDetails(@PathVariable int farmerid, @PathVariable int productid) {
		return ResponseEntity.ok(fService.getProductDetails(farmerid, productid));
	}

	@GetMapping("/allproducts")
	public ResponseEntity<List<StockDetails>> productList() {
		return ResponseEntity.ok(fService.getAllProduct());
	}
}
