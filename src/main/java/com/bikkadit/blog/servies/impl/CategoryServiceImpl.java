package com.bikkadit.blog.servies.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bikkadit.blog.entities.Category;
import com.bikkadit.blog.exceptions.ResourceNotFoundException;
import com.bikkadit.blog.helper.CategoryConstants;
import com.bikkadit.blog.payloads.CategoryDto;
import com.bikkadit.blog.repositories.CategoryRepo;
import com.bikkadit.blog.servies.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		logger.info("Initiating the dao call for save new Category");
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category addedCat = this.categoryRepo.save(category);
		logger.info("Completed the dao call for save new  Category");
		return this.modelMapper.map(addedCat, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		logger.info("Initiating the dao call for update the Category with categoryId{}:", categoryId);
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryConstants.NOT_FOUND + categoryId));
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		Category updatedCat = this.categoryRepo.save(category);
		logger.info("Completed the dao call for update the Category with categoryId{}:", categoryId);
		return this.modelMapper.map(updatedCat, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		logger.info("Initiating the dao call for delete the Category with categoryId{}:", categoryId);
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryConstants.NOT_FOUND + categoryId));
		this.categoryRepo.delete(category);
		logger.info("Completed the dao call for delete the Category with categoryId{}:", categoryId);
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		logger.info("Initiating the dao call for get the Category with categoryId{}:", categoryId);
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryConstants.NOT_FOUND + categoryId));
		logger.info("Completed the dao call for get the Category with categoryId{}:", categoryId);
		return this.modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		logger.info("Initiating the dao call for get all the Categories");
		List<Category> categories = this.categoryRepo.findAll();
		List<CategoryDto> collect = categories.stream().map((cat) -> this.modelMapper.map(cat, CategoryDto.class))
				.collect(Collectors.toList());
		logger.info("Completed the dao call for get all the Categories");
		return collect;
	}

}
