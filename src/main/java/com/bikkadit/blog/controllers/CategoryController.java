package com.bikkadit.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikkadit.blog.helper.CategoryConstants;
import com.bikkadit.blog.payloads.ApiResponse;
import com.bikkadit.blog.payloads.CategoryDto;
import com.bikkadit.blog.servies.CategoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categories")
@Slf4j
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * @author AMIT SHARMA
	 * @apiNote Create a New Category
	 * @param categoryDto
	 * @return CategoryDto
	 * @since 1.0
	 */
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		log.info("Entering the request for save new Category");
		CategoryDto category = this.categoryService.createCategory(categoryDto);
		log.info("Completed the request for save new Category");
		return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
	}

	/**
	 * @author AMIT SHARMA
	 * @apiNote Update the Category
	 * @param categoryDto
	 * @param categoryId
	 * @return CategoryDto
	 * @since 1.0
	 */
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable Integer categoryId) {
		log.info("Entering the request for update the Category with categoryId{}:", categoryId);
		CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
		log.info("Completed the request for update the Category with categoryId{}:", categoryId);
		return new ResponseEntity<CategoryDto>(updateCategory, HttpStatus.OK);
	}

	/**
	 * @author AMIT SHARMA
	 * @apiNote Delete Category by categoryId
	 * @param categoryId
	 * @return ApiResponse
	 * @since 1.0
	 */
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId) {
		log.info("Entering the request for delete the Category with categoryId{}:", categoryId);
		this.categoryService.deleteCategory(categoryId);
		log.info("Completed the request for delete the Category with categoryId{}:", categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(CategoryConstants.Category_DELETED, true),
				HttpStatus.OK);
	}

	/**
	 * @author AMIT SHARMA
	 * @apiNote Get Category by categoryId
	 * @param categoryId
	 * @return CategoryDto
	 * @since 1.0
	 */
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
		log.info("Entering the request for get the Category with categoryId{}:", categoryId);
		CategoryDto category = this.categoryService.getCategory(categoryId);
		log.info("Completed the request for get the Category with categoryId{}:", categoryId);
		return new ResponseEntity<CategoryDto>(category, HttpStatus.OK);
	}

	/**
	 * @author AMIT SHARMA
	 * @apiNote To Get All The Categories
	 * @return CategoryDto
	 * @since 1.0
	 */
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {
		log.info("Entering the request for get all the Categories");
		List<CategoryDto> categories = this.categoryService.getAllCategories();
		log.info("Completed the request for get all the Categories");
		return new ResponseEntity<List<CategoryDto>>(categories, HttpStatus.OK);
	}
}
