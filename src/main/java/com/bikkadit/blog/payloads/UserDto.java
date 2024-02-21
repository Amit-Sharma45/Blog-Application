package com.bikkadit.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.bikkadit.blog.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

	private Integer id;
	
	@NotEmpty
	@Size(min = 4,message = "Username must be contains atleast 4 characters !!")
	private String name;
	
	@Email(message = "Email Address is not valid !!")
	private String email;
	
	@NotEmpty
	@Size(min = 4,max = 12,message = "Password must be min of 4 and max of 12")
	private String password;
	
	@NotEmpty
	private String about;
	
	private Set<RoleDto> roles = new HashSet<>();
	
}
