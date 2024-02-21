package com.bikkadit.blog.servies;

import java.util.List;
import com.bikkadit.blog.payloads.PostDto;
import com.bikkadit.blog.payloads.PostResponse;

public interface PostService {

//	create Post
	PostDto createPost(PostDto postDto, Integer categoryId, Integer userId);

//	get Post by id
	PostDto getPostById(Integer postId);

//	get Post by category
	List<PostDto> getPostByCategory(Integer categoryId);

//	get Post by user
	List<PostDto> getPostByUser(Integer userId);

//	get all Post
	List<PostDto> getAllPosts();
	
//	delete Post
	void deletePost(Integer postId);

//	update Post
	PostDto updatePost(PostDto postDto, Integer postId);

//	pagination
	PostResponse getAllPostsPagi(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

	List<PostDto> searchPosts(String keywords);
}
