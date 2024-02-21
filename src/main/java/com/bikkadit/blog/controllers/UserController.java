package com.bikkadit.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikkadit.blog.helper.UserAppConstants;
import com.bikkadit.blog.payloads.ApiResponse;
import com.bikkadit.blog.payloads.UserDto;
import com.bikkadit.blog.servies.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * @author AMIT
	 * @apiNote To save or create new User
	 * @param userDto
	 * @return UserDto
	 * @since 1.0
	 */
	@PostMapping("/")
	public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
		log.info("Entering the Request for Save user record");
		UserDto user = this.userService.addUser(userDto);
		log.info("Completed the Request for Save user record");
		return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
	}

	/**
	 * @author AMIT
	 * @apiNote To update the user
	 * @param userDto
	 * @param userId
	 * @return UserDto
	 * @since 1.0
	 */
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId) {
		log.info("Entering the Request for update the user record with userId{}:", userId);
		UserDto updateUser = this.userService.updateUser(userDto, userId);
		log.info("Completed the Request for update the user record with userId{}:", userId);
		return new ResponseEntity<UserDto>(updateUser, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To Get the User by User Id
	 * @param userId
	 * @return UserDto
	 * @since 1.0
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId) {
		log.info("Entering the Request for get user record by userId{}:", userId);
		UserDto userDto = this.userService.getUserById(userId);
		log.info("Completed the Request for get user record by userId{}:", userId);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To Get All Users
	 * @return UserDto
	 * @since 1.0
	 */
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUser() {
		log.info("Entering the Request for get All records of Users");
		List<UserDto> allUsers = this.userService.getAllUsers();
		log.info("Completed the Request for get All records of Users");
		return new ResponseEntity<List<UserDto>>(allUsers, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To delete User by UserId
	 * @param userId
	 * @return ApiResponse
	 * @since 1.0
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) {
		log.info("Entering the Request for delete the user record by userId{}:", userId);
		this.userService.deleteUser(userId);
		log.info("Completed the Request for delete the user record by userId{}:", userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(UserAppConstants.USER_DELETED + userId, true),
				HttpStatus.OK);

	}

}
