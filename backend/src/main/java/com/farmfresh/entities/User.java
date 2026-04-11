package com.farmfresh.entities;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "farm_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(unique = true, length = 50)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	@JsonIgnore
	private String passwordHash;

	@Column(name = "phone_no", length = 15)
	private String phoneNo;

	@Column(name = "phone_verified", nullable = false)
	private boolean phoneVerified;

	@Column(name = "phone_otp_hash", length = 64)
	private String phoneOtpHash;

	@Column(name = "phone_otp_expires_at")
	private Instant phoneOtpExpiresAt;

	@Column(name = "Address", length = 200)
	private String address;

	@Column(length = 20)
	private String firstname;

	@Column(length = 20)
	private String lastname;

	@Column(name = "is_admin")
	private boolean isadmin;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Orders> orders;

	public User() {
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public boolean isPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(boolean phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public String getPhoneOtpHash() {
		return phoneOtpHash;
	}

	public void setPhoneOtpHash(String phoneOtpHash) {
		this.phoneOtpHash = phoneOtpHash;
	}

	public Instant getPhoneOtpExpiresAt() {
		return phoneOtpExpiresAt;
	}

	public void setPhoneOtpExpiresAt(Instant phoneOtpExpiresAt) {
		this.phoneOtpExpiresAt = phoneOtpExpiresAt;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public boolean isIsadmin() {
		return isadmin;
	}

	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
}
