package com.farmfresh.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "order_item", nullable = false, length = 20)
	private String orderItem;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false, precision = 2)
	private double amount;

	@ManyToOne
	@JoinColumn(name = "farmer_id")
	private Farmer farmer;

	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private Orders orders;

	public OrderDetails() {
		System.out.println("OrderDetails Constructor invoked");
	}

	public OrderDetails(Integer id, String orderItem, int quantity, double amount, Orders orders) {
		super();
		this.id = id;
		this.orderItem = orderItem;
		this.quantity = quantity;
		this.amount = amount;
		this.orders = orders;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(String orderItem) {
		this.orderItem = orderItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderDetails [orderItem=" + orderItem + ", quantity=" + quantity + ", amount=" + amount + ", farmer="
				+ farmer + ", orders=" + orders + "]";
	}

}
