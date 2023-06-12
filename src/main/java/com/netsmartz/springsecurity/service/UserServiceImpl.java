package com.netsmartz.springsecurity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.netsmartz.springsecurity.model.UserDetails;
import com.netsmartz.springsecurity.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public UserDetails createUser(UserDetails user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		return userRepo.save(user);
	}

	public List<UserDetails> getAllUser() {
		List<UserDetails> user = (List<UserDetails>) userRepo.findAll();
		return user;
	}

	@Override
	public boolean checkEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public Page<UserDetails> getAll(PageRequest pageRequest) {
		Page<UserDetails> page = userRepo.findAll(pageRequest);
		return page;
	}

	@Override
	public Page<UserDetails> getPaginated(int page, int pageSize) {
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		return userRepo.findAll(pageRequest);
	}

	public void updateResetPasswordToken(String resetPasswordToken, String email)
			throws UsernameNotFoundException {
		UserDetails user = userRepo.findByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(resetPasswordToken);
			userRepo.save(user);
		} else {
			throw new UsernameNotFoundException("Could not find any customer with the email " + email);
		}
	}

	public UserDetails getResetPasswordToken(String token) {
		return userRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(UserDetails user, String newPassword) {
		BCryptPasswordEncoder pEncoder = new BCryptPasswordEncoder();
		String encoded = pEncoder.encode(newPassword);
		user.setPassword(encoded);

		user.setResetPasswordToken(null);
		userRepo.save(user);
	}
}
