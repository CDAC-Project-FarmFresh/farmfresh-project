package com.farmfresh.newservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.farmfresh.custom_exceptions.ResourceNotFoundException;
import com.farmfresh.entities.Category;
import com.farmfresh.entities.Farmer;
import com.farmfresh.entities.OrderDetails;
import com.farmfresh.entities.StockDetails;
import com.farmfresh.entities.User;
import com.farmfresh.repository.CategoryDetailsRepository;
import com.farmfresh.repository.FarmerDetailsRepository;
import com.farmfresh.repository.OrderDetailsRepository;
import com.farmfresh.repository.StockDetailsRepository;
import com.farmfresh.repository.UserDetailsRepository;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	private final FarmerDetailsRepository fRepo;
	private final StockDetailsRepository sRepo;
	private final CategoryDetailsRepository cRepo;
	private final UserDetailsRepository uRepo;
	private final OrderDetailsRepository orderDetailsRepository;

	@PersistenceContext
	private EntityManager mgr;

	public AdminServiceImpl(FarmerDetailsRepository fRepo, StockDetailsRepository sRepo,
			CategoryDetailsRepository cRepo, UserDetailsRepository uRepo,
			OrderDetailsRepository orderDetailsRepository) {
		this.fRepo = fRepo;
		this.sRepo = sRepo;
		this.cRepo = cRepo;
		this.uRepo = uRepo;
		this.orderDetailsRepository = orderDetailsRepository;
	}

	@Override
	public Farmer GetFarmerDetails(Integer farmerid) {
		return fRepo.getFarmerDetailsById(farmerid);
	}

	@Override
	public Farmer AddFarmer(Farmer farmer) {
		return fRepo.save(farmer);
	}

	@Override
	public boolean RemoveFarmer(Integer farmerid) {
		if (fRepo.existsById(farmerid)) {
			fRepo.deleteById(farmerid);
			return true;
		}
		return false;
	}

	@Override
	public boolean UpdateFarmer(Farmer farmer) {
		mgr.merge(farmer);
		return true;
	}

	@Override
	public StockDetails GetProductDetails(Integer productid) {
		return sRepo.findById(productid).orElse(null);
	}

	@Override
	public boolean AddProduct(Integer farmerid, StockDetails product) {
		Farmer farmer = fRepo.getFarmerDetailsById(farmerid);
		product.setFarmer1(farmer);
		sRepo.save(product);
		return true;
	}

	@Override
	public boolean RemoveProduct(Integer productid) {
		if (sRepo.existsById(productid)) {
			sRepo.deleteById(productid);
			return true;
		}
		return false;
	}

	@Override
	public StockDetails UpdateProduct(StockDetails product) {
		return sRepo.save(product);
	}

	@Override
	public Category GetCategory(Integer catid) {
		return cRepo.findByCategoryId(catid);
	}

	@Override
	public Category SetCategory(String category) {
		Category newCategory = new Category();
		newCategory.setCategoryName(category);
		return cRepo.save(newCategory);
	}

	@Override
	public boolean RemoveCategory(Integer catid) {
		if (cRepo.existsById(catid)) {
			cRepo.deleteById(catid);
			return true;
		}
		return false;
	}

	@Override
	public String saveImage(Integer productId, MultipartFile imgFile) throws IOException {
		StockDetails s = sRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		String path = imgFile.getOriginalFilename();
		s.setImagePath(path);
		Files.copy(imgFile.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
		sRepo.save(s);
		return "file copied";
	}

	@Override
	public byte[] restoreImage(Integer pId) throws IOException {
		StockDetails s = sRepo.findById(pId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		String path = s.getImagePath();
		if (path != null) {
			return Files.readAllBytes(Paths.get(path));
		}
		throw new ResourceNotFoundException("Image not yet assigned for " + s.getStockItem());
	}

	@Override
	public List<Category> getAllCategory() {
		return cRepo.findAll();
	}

	@Override
	public List<OrderDetails> getAllOrders() {
		return orderDetailsRepository.findAll();
	}

	@Override
	public List<User> getAllUser() {
		return uRepo.findAll();
	}

	@Override
	public User UpdateUser(User user) {
		return uRepo.save(user);
	}

	@Override
	public byte[] restoreImageAgain(String productName) throws IOException {
		StockDetails s = sRepo.findByStockItem(productName);
		if (s == null) {
			throw new ResourceNotFoundException("Product not found");
		}
		String path = s.getImagePath();
		if (path != null) {
			return Files.readAllBytes(Paths.get(path));
		}
		throw new ResourceNotFoundException("Image not yet assigned for " + s.getStockItem());
	}
}
