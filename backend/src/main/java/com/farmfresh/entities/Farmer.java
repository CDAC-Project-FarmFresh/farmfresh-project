package com.farmfresh.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "farmer")
public class Farmer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "farmer_id")
	private Integer farmerId;

	@Column(length = 20)
	private String firstname;

	@Column(length = 20)
	private String lastname;

	@Column(length = 25, unique = true)
	private String email;

	@Column(name = "phone_no", length = 15, unique = true)
	private String phoneNo;

	@Column(length = 200)
	private String address;

	@OneToMany(mappedBy = "farmer", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private List<OrderDetails> orderDetails;

	@OneToMany(mappedBy = "farmer1", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonInclude(Include.NON_NULL)
	private List<StockDetails> stock = new ArrayList<StockDetails>();

	public Farmer() {
		System.out.println("Farmer Constructor invoked");
	}

	public Farmer(Integer farmerId, String firstname, String lastname, String email, String phone, String address) {
		super();
		this.farmerId = farmerId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phoneNo = phone;
		this.address = address;
	}

	public Farmer(Integer farmerId, String firstname, String lastname) {
		super();
		this.farmerId = farmerId;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public Integer getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Integer farmerId) {
		this.farmerId = farmerId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OrderDetails> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetails> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public List<StockDetails> getStock() {
		return stock;
	}

	public void setStock(List<StockDetails> stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Farmer [farmerId=" + farmerId + ", firstname=" + firstname + ", lastname=" + lastname + ", email="
				+ email + ", phoneNo=" + phoneNo + ", address=" + address + ", orderDetails=" + orderDetails
				+ ", stock=" + stock + "]";
	}

}
