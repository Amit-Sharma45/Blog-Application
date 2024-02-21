package com.bikkadit.blog.servies.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bikkadit.blog.entities.Comment;
import com.bikkadit.blog.entities.Post;
import com.bikkadit.blog.exceptions.ResourceNotFoundException;
import com.bikkadit.blog.helper.PostConstants;
import com.bikkadit.blog.payloads.CommentDto;
import com.bikkadit.blog.repositories.CommentRepo;
import com.bikkadit.blog.repositories.PostRepo;
import com.bikkadit.blog.servies.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(PostConstants.NOT_FOUND + postId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		Comment savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {

		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Record not Found with id " + commentId));

		this.commentRepo.delete(comment);
	}

}
