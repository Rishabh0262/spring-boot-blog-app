package com.gtech.springboot_blog_rest_api.service.impl;

import com.gtech.springboot_blog_rest_api.entity.Comment;
import com.gtech.springboot_blog_rest_api.payload.CommentDto;
import com.gtech.springboot_blog_rest_api.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl {
    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment(commentDto.getId(), commentDto.getName(), commentDto.getEmail(), commentDto.getBody() , null);

        return comment;
    }

}
