package com.farmfresh.newcontroller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmfresh.newservice.AdminService;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

	private final AdminService adminService;

	public CatalogController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping(value = "/products/{productId}/image", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<byte[]> productImage(@PathVariable int productId) throws IOException {
		return ResponseEntity.ok(adminService.restoreImage(productId));
	}

	@GetMapping(value = "/products/by-name/{name}/image", produces = { MediaType.IMAGE_GIF_VALUE,
			MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<byte[]> productImageByName(@PathVariable String name) throws IOException {
		return ResponseEntity.ok(adminService.restoreImageAgain(name));
	}
}
