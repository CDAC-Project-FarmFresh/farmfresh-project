package com.farmfresh.newservice;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.farmfresh.entities.Category;
import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.OrderDetails;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.entities.User;

public interface AdminService {

	public Farmer GetFarmerDetails(Integer farmerid);

	public Farmer AddFarmer(Farmer farmer);

	public boolean RemoveFarmer(Integer farmerid);

	public boolean UpdateFarmer(Farmer farmer);

	public StockDetails GetProductDetails(Integer productid);

	public boolean AddProduct(Integer farmerid, StockDetails product);

	public boolean RemoveProduct(Integer productid);

	public StockDetails UpdateProduct(StockDetails product);

	public Category GetCategory(Integer catid);

	public Category SetCategory(String category);

	public boolean RemoveCategory(Integer catid);

	public List<Category> getAllCategory();

	public List<OrderDetails> getAllOrders();

	public List<User> getAllUser();

	User UpdateUser(User user);

	String saveImage(Integer productId, MultipartFile imgFile) throws IOException;

	byte[] restoreImage(Integer empId) throws IOException;

	byte[] restoreImageAgain(String productName) throws IOException;
}
