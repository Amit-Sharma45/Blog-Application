package com.bikkadit.blog.servies.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bikkadit.blog.entities.Role;
import com.bikkadit.blog.entities.User;
import com.bikkadit.blog.exceptions.ResourceNotFoundException;
import com.bikkadit.blog.helper.UserAppConstants;
import com.bikkadit.blog.payloads.UserDto;
import com.bikkadit.blog.repositories.RoleRepo;
import com.bikkadit.blog.repositories.UserRepo;
import com.bikkadit.blog.servies.UserService;


@Service
//@Slf4j
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public UserDto addUser(UserDto userDto) {
		logger.info("Initiating the dao call for save the user");
		User user = this.modelMapper.map(userDto, User.class);
		User savedUser = this.userRepo.save(user);
		logger.info("Completed the dao call for save the user");
		return this.modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		logger.info("Initiating the dao call for update the user data with userId{}:", userId);
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(UserAppConstants.NOT_FOUND + userId));

		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		User updatedUser = this.userRepo.save(user);
		logger.info("Completed the Dao calling to update the user data with userId{}:", userId);
		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		logger.info("Initiating the dao call for get Single user record by userId{}:", userId);
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(UserAppConstants.NOT_FOUND + userId));
		logger.info("Completed the Dao call for get single user record by userId{}:", userId);
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		logger.info("Initiating the dao call for get All records of Users");
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		logger.info("Completed Dao Call for get All records of Users");
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		logger.info("Initiating the dao call for delete the user record by userId{}:", userId);
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(UserAppConstants.NOT_FOUND + userId));
		this.userRepo.delete(user);
		logger.info("Completed the Dao call for delete the user record by userId{}:", userId);
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		Role role = this.roleRepo.findById(UserAppConstants.NORMAL_USER).get();
			user.getRoles().add(role);
			User newUser = this.userRepo.save(user);
			
		return this.modelMapper.map(newUser, UserDto.class);
	}

}
