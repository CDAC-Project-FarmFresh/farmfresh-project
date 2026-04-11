package com.farmfresh.dto;

import com.farmfresh.entities.User;

public class UserResponseDto {

	private Integer userId;
	private String email;
	private String phoneNo;
	private boolean phoneVerified;
	private String address;
	private String firstname;
	private String lastname;
	private boolean isadmin;

	public static UserResponseDto fromEntity(User u) {
		UserResponseDto d = new UserResponseDto();
		d.setUserId(u.getUserId());
		d.setEmail(u.getEmail());
		d.setPhoneNo(u.getPhoneNo());
		d.setPhoneVerified(u.isPhoneVerified());
		d.setAddress(u.getAddress());
		d.setFirstname(u.getFirstname());
		d.setLastname(u.getLastname());
		d.setIsadmin(u.isIsadmin());
		return d;
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
}
