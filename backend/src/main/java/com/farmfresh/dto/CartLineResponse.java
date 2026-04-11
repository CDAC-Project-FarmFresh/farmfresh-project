package com.farmfresh.dto;

public class CartLineResponse {

	private Long lineId;
	private int productId;
	private String item;
	private int qty;
	private double price;
	private double amount;
	private int farmerId;

	public CartLineResponse() {
	}

	public CartLineResponse(Long lineId, int productId, String item, int qty, double price, double amount,
			int farmerId) {
		this.lineId = lineId;
		this.productId = productId;
		this.item = item;
		this.qty = qty;
		this.price = price;
		this.amount = amount;
		this.farmerId = farmerId;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(int farmerId) {
		this.farmerId = farmerId;
	}
}
