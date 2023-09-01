package com.netsmartz.springsecurity.model;

import lombok.Getter;
import org.springframework.stereotype.Service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;

@Getter
@Entity
@Service
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NonNull
	private String fullname;
	@NonNull
	private String email;
	@NonNull
	private String address;
	@NonNull
	private String qualification;
	@NonNull
	private String password;
	@NonNull
	private String role;
	@NonNull
	private String resetPasswordToken;

	public UserDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDetails(Integer id, String fullname, String email, String address, String qualification, String password) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.email = email;
		this.address = address;
		this.qualification = qualification;
		this.password = password;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	@Override
	public String toString() {
		return "UserDetails [id=" + id + ", fullname=" + fullname + ", email=" + email + ", address=" + address
				+ ", qualification=" + qualification + "]";
	}

	public static boolean isNonEmptyAndNonBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

}
