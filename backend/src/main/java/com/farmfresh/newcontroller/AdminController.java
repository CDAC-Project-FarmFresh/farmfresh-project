package com.farmfresh.newcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.farmfresh.entities.Category;
import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.OrderDetails;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.entities.User;
import com.farmfresh.newservice.AdminService;
import com.farmfresh.newservice.FarmerService;
import com.farmfresh.newservice.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final AdminService service;
	private final FarmerService fService;
	private final UserService uService;

	public AdminController(AdminService service, FarmerService fService, UserService uService) {
		this.service = service;
		this.fService = fService;
		this.uService = uService;
	}

	@PostMapping("/newfarmer")
	public ResponseEntity<Farmer> addNewFarmer(@RequestBody Farmer farmer) {
		service.AddFarmer(farmer);
		return ResponseEntity.ok(farmer);
	}

	@PostMapping("/newproduct/{farmerid}")
	public ResponseEntity<String> addNewProduct(@PathVariable int farmerid, @RequestBody StockDetails product) {
		service.AddProduct(farmerid, product);
		return ResponseEntity.ok("Product Added Successfully");
	}

	@PostMapping("/{productid}/image")
	public ResponseEntity<String> uploadImage(@PathVariable int productid, @RequestParam MultipartFile imgFile)
			throws IOException {
		String msg = service.saveImage(productid, imgFile);
		return ResponseEntity.ok(msg);
	}

	@GetMapping(value = "/{productid}", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<byte[]> downloadImage(@PathVariable int productid) throws IOException {
		return ResponseEntity.ok(service.restoreImage(productid));
	}

	@DeleteMapping("/farmers/{farmerid}")
	public ResponseEntity<String> deleteFarmer(@PathVariable int farmerid) {
		service.RemoveFarmer(farmerid);
		return ResponseEntity.ok("Farmer Removed Successfully");
	}

	@DeleteMapping("/products/{productid}")
	public ResponseEntity<String> deleteProduct(@PathVariable int productid) {
		service.RemoveProduct(productid);
		return ResponseEntity.ok("Product Removed Successfully");
	}

	@PutMapping("/updateproduct/{productid}")
	public ResponseEntity<String> updateProduct(@PathVariable int productid, @RequestParam("stockitem") String stockitem,
			@RequestParam("priceperunit") float priceperunit, @RequestParam("catid") int catid,
			@RequestParam("quantity") int quantity) {
		StockDetails product = service.GetProductDetails(productid);
		if (product != null) {
			Category category = service.GetCategory(catid);
			product.setPricePerUnit(priceperunit);
			product.setStockItem(stockitem);
			product.setCategory(category);
			product.setQuantity(quantity);
			service.UpdateProduct(product);
			return ResponseEntity.ok("Product Updated");
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/updatefarmer/{farmerid}")
	public ResponseEntity<Farmer> updateFarmer(@PathVariable int farmerid, @RequestBody Farmer f) {
		Farmer farmer = fService.getFarmerDetails(farmerid);
		if (farmer != null) {
			farmer.setFirstname(f.getFirstname());
			farmer.setLastname(f.getLastname());
			farmer.setEmail(f.getEmail());
			farmer.setAddress(f.getAddress());
			farmer.setPhoneNo(f.getPhoneNo());
			service.UpdateFarmer(farmer);
			return ResponseEntity.ok(farmer);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/addcategory/{category}")
	public ResponseEntity<String> addCategory(@PathVariable String category) {
		service.SetCategory(category);
		return ResponseEntity.ok("New Category: " + category + " Added");
	}

	@DeleteMapping("/categories/{catid}")
	public ResponseEntity<String> removeCategory(@PathVariable int catid) {
		service.RemoveCategory(catid);
		return ResponseEntity.ok("Category Removed");
	}

	@GetMapping("/categorylist")
	public ResponseEntity<List<Category>> categoryList() {
		return ResponseEntity.ok(service.getAllCategory());
	}

	@GetMapping("/allorders")
	public ResponseEntity<List<OrderDetails>> getAllOrders() {
		return ResponseEntity.ok(service.getAllOrders());
	}

	@GetMapping("/allusers")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(service.getAllUser());
	}

	@PostMapping("/updateuser/{userId}")
	public ResponseEntity<User> updateUser(@PathVariable int userId, @RequestBody User user) {
		User u = uService.getUserDetails(userId);
		u.setFirstname(user.getFirstname());
		u.setLastname(user.getLastname());
		u.setEmail(user.getEmail());
		u.setAddress(user.getAddress());
		u.setPhoneNo(user.getPhoneNo());
		service.UpdateUser(u);
		return ResponseEntity.ok(u);
	}

	@GetMapping("/userdetails/{userId}")
	public ResponseEntity<User> getUserDetails(@PathVariable int userId) {
		return ResponseEntity.ok(uService.getUserDetails(userId));
	}

	@GetMapping(value = "/image/{productName}", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<byte[]> downloadImageAgain(@PathVariable String productName) throws IOException {
		return ResponseEntity.ok(service.restoreImageAgain(productName));
	}
}
