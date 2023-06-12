package com.netsmartz.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsmartz.springsecurity.model.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

	public boolean existsByEmail(String email);

	public UserDetails findByEmail(String email);

	public UserDetails findByResetPasswordToken(String resetPasswordToken);

}
