package com.netsmartz.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.netsmartz.springsecurity.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		com.netsmartz.springsecurity.model.UserDetails user = repository.findByEmail(email);
		if (user != null) {
			return new CustomUserDetails(user);
		}
		throw new UsernameNotFoundException("User Not Found");
	}

}
