package com.bikkadit.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bikkadit.blog.helper.PostConstants;
import com.bikkadit.blog.helper.UserAppConstants;
import com.bikkadit.blog.payloads.ApiResponse;
import com.bikkadit.blog.payloads.PostDto;
import com.bikkadit.blog.payloads.PostResponse;
import com.bikkadit.blog.servies.FileService;
import com.bikkadit.blog.servies.PostService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/")
@Slf4j
public class PostConroller {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	/**
	 * @author AMIT
	 * @apiNote To create the New Post
	 * @param postDto
	 * @param categoryId
	 * @param userId
	 * @return PostDto
	 * @since 1.0
	 */
	@PostMapping("/userId/{userId}/categoryId/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer categoryId,
			@PathVariable Integer userId) {
		log.info("Entering the Request for create new post");
		PostDto post = this.postService.createPost(postDto, categoryId, userId);
		log.info("Completed the Request for create new post");
		return new ResponseEntity<PostDto>(post, HttpStatus.CREATED);
	}

	/**
	 * @author AMIT
	 * @apiNote To get Post by PostId
	 * @param postId
	 * @return PostDto
	 * @since 1.0
	 */
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		log.info("Entering the Request for get the post by postId{}:", postId);
		PostDto postDto = this.postService.getPostById(postId);
		log.info("Completed the Request for get the post by postId{}:", postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To Get Post By Category
	 * @param categoryId
	 * @return List of PostDto
	 * @since 1.0
	 */
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {
		log.info("Entering the Request for get all the posts with categoryId{}:", categoryId);
		List<PostDto> posts = this.postService.getPostByCategory(categoryId);
		log.info("Completed the Request for get all the posts with categoryId{}:", categoryId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To get List of posts By User
	 * @param userId
	 * @return List Of PostDto
	 * @since 1.0
	 */
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {
		log.info("Entering the Request for get all the posts with userId{}:", userId);
		List<PostDto> posts = this.postService.getPostByUser(userId);
		log.info("Completed the Request for get all the posts with userId{}:", userId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To get All the Posts
	 * @return List of PostDto
	 * @since 1.0
	 */
	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPosts() {
		log.info("Entering the Request for get all the posts ");
		List<PostDto> posts = this.postService.getAllPosts();
		log.info("Completed the Request for get all the posts");
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote Delete Post By postId
	 * @param postId
	 * @return ApiResponse
	 * @since 1.0
	 */
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		log.info("Entering the Request for delete the post with postId{}:", postId);
		this.postService.deletePost(postId);
		log.info("Completed the Request for delete the post with postId{}:", postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(PostConstants.POST_DELETED, true), HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote To Update The Post
	 * @param postDto
	 * @param postId
	 * @return PostDto
	 * @since 1.0
	 */
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
		log.info("Entering the Request for update the post with postId{}:", postId);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		log.info("Completed the Request for update the post with postId{}:", postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote Apply Pagination
	 * @param pageNumber
	 * @param pageSize
	 * @return List Of PostDto
	 * @since 1.0
	 */
	@GetMapping("/posts/Page")
	public ResponseEntity<PostResponse> getAllPostsPagi(
			@RequestParam(defaultValue = UserAppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = UserAppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = UserAppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = UserAppConstants.SORT_DIR, required = false) String sortDir) {
		log.info("Entering the request for get all posts for pagination");
		PostResponse pagi = this.postService.getAllPostsPagi(pageNumber, pageSize, sortBy, sortDir);
		log.info("Completed the request for get all posts for pagination");
		return new ResponseEntity<PostResponse>(pagi, HttpStatus.OK);
	}

	/**
	 * @author AMIT
	 * @apiNote to search by keywords
	 * @param keywords
	 * @return
	 * @since 1.0
	 */
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String keywords) {
		log.info("Entering the request for get all posts with keywords{}: ", keywords);
		List<PostDto> posts = this.postService.searchPosts(keywords);
		log.info("Completed the request for get all posts with keywords{}: ", keywords);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}

	@PostMapping("/posts/image/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam MultipartFile image, @PathVariable Integer postId)
			throws IOException {
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);
		PostDto postDto2 = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(postDto2, HttpStatus.OK);
	}

	@GetMapping("/posts/image/{imageName}")
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
