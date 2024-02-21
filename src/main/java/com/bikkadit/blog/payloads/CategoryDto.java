package com.bikkadit.blog.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;

	@NotBlank
	@Size(min=5,message = "Title should be min of 5 characters !!")
	private String categoryTitle;
	
	@NotBlank
	@Size(min = 10,message = "Description should be min of 10 Characters !!")
	private String categoryDescription;
}
