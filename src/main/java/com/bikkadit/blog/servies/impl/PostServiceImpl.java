package com.bikkadit.blog.servies.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bikkadit.blog.entities.Category;
import com.bikkadit.blog.entities.Post;
import com.bikkadit.blog.entities.User;
import com.bikkadit.blog.exceptions.ResourceNotFoundException;
import com.bikkadit.blog.helper.CategoryConstants;
import com.bikkadit.blog.helper.PostConstants;
import com.bikkadit.blog.helper.UserAppConstants;
import com.bikkadit.blog.payloads.PostDto;
import com.bikkadit.blog.payloads.PostResponse;
import com.bikkadit.blog.repositories.CategoryRepo;
import com.bikkadit.blog.repositories.PostRepo;
import com.bikkadit.blog.repositories.UserRepo;
import com.bikkadit.blog.servies.PostService;

@Service
public class PostServiceImpl implements PostService {
	Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

//	Create new Post
	@Override
	public PostDto createPost(PostDto postDto, Integer categoryId, Integer userId) {
		logger.info("Initiating the dao call for Creating new post");
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryConstants.NOT_FOUND + categoryId));
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(UserAppConstants.NOT_FOUND + userId));
		Post post = this.modelMapper.map(postDto, Post.class);

		post.setImageName("Amit.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);
		logger.info("Completed the dao call for Creating new Post");
		return this.modelMapper.map(newPost, PostDto.class);
	}

//	Get Single Post
	@Override
	public PostDto getPostById(Integer postId) {
		logger.info("Initiating the dao call for get the post with postId{}:", postId);
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(PostConstants.NOT_FOUND + postId));
		logger.info("Completed the dao call for get the post with postId{}:", postId);
		return this.modelMapper.map(post, PostDto.class);
	}

//	Get Post by Category
	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {
		logger.info("Initiating the dao call for get all posts with categoryId{}:", categoryId);
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryConstants.NOT_FOUND + categoryId));
		List<Post> posts = this.postRepo.findByCategory(category);
		List<PostDto> postDto = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		logger.info("Completed the dao call for get all posts with categoryId{}:", categoryId);
		return postDto;
	}

//	Get Post by User
	@Override
	public List<PostDto> getPostByUser(Integer userId) {
		logger.info("Initiating the dao call for get all the posts with userId{}:", userId);
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(UserAppConstants.NOT_FOUND + userId));
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postDto = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		logger.info("Completed the dao call for get all the posts with userId{}:", userId);
		return postDto;
	}

//	Get All Posts
	@Override
	public List<PostDto> getAllPosts() {
		logger.info("Initiating the dao call for get all the posts");
		List<Post> posts = this.postRepo.findAll();
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		logger.info("Completed the dao call for get all the posts");
		return postDtos;
	}

//	Delete Post
	@Override
	public void deletePost(Integer postId) {
		logger.info("Initiating the dao call for delete the post with postId{}:", postId);
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(PostConstants.NOT_FOUND + postId));
		this.postRepo.delete(post);
		logger.info("Completed the dao call for delete the post with postId{}:", postId);
	}

//	Update Post
	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		logger.info("Initiating the dao call for update the post with postId{}:", postId);
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(PostConstants.NOT_FOUND + postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post postUpdated = this.postRepo.save(post);
		logger.info("Completed the dao call for update the post with postId{}:", postId);
		return this.modelMapper.map(postUpdated, PostDto.class);
	}

	@Override
	public PostResponse getAllPostsPagi(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		logger.info("Initiating the dao call for get all posts with pagination");
		Sort sort = null;
		if (sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> page = this.postRepo.findAll(p);
		List<Post> posts = page.getContent();
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(page.getNumber());
		postResponse.setPageSize(page.getSize());
		postResponse.setTotalElements(page.getTotalElements());
		postResponse.setTotalPages(page.getTotalPages());
		postResponse.setLastPage(page.isLast());
		logger.info("Completed the dao call for get all posts with pagination");
		return postResponse;
	}

	@Override
	public List<PostDto> searchPosts(String keywords) {
		logger.info("Initiating the dao call for get all posts with keywords{}: ", keywords);
		List<Post> posts = this.postRepo.findByTitleContaining(keywords);
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		logger.info("Completed the dao call for get all posts with keywords{}: ", keywords);
		return postDtos;
	}

}
