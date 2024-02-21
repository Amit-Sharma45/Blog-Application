package com.bikkadit.blog.servies;

import java.util.List;
import com.bikkadit.blog.payloads.UserDto;

public interface UserService {

	UserDto registerNewUser(UserDto userDto);

	UserDto addUser(UserDto userDto);

	UserDto updateUser(UserDto userDto, Integer userId);

	UserDto getUserById(Integer userId);

	List<UserDto> getAllUsers();

	void deleteUser(Integer userId);
}
