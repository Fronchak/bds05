package com.devsuperior.movieflix.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dtos.user.UserOutputDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.mappers.UserMapper;
import com.devsuperior.movieflix.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private AuthService authService;
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Transactional(readOnly = true)
	public UserOutputDTO getLoggedUser() {
		User entity = authService.authenticated();
		if(entity == null) {
			throw new ResourceNotFoundException("User not found");
		}
		return mapper.convertEntityToOutputDTO(entity);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if(user == null) {
			logger.error("User not found bu email: " + username);
			throw new UsernameNotFoundException("User not found bu email: " + username);
		}
		logger.info("User found by email: " + username);
		return user;
	}
}
