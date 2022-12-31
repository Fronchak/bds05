package com.devsuperior.movieflix.mappers;

import org.springframework.stereotype.Service;

import com.devsuperior.movieflix.dtos.user.UserOutputDTO;
import com.devsuperior.movieflix.entities.User;

@Service
public class UserMapper {

	public UserOutputDTO convertEntityToOutputDTO(User entity) {
		UserOutputDTO dto = new UserOutputDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setEmail(entity.getEmail());
		return dto;
	}
}
