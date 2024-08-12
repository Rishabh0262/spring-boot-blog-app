package com.gtech.springboot_blog_rest_api.service.impl;

import com.gtech.springboot_blog_rest_api.entity.Comment;
import com.gtech.springboot_blog_rest_api.entity.Post;
import com.gtech.springboot_blog_rest_api.exception.BlogAPIException;
import com.gtech.springboot_blog_rest_api.exception.ResourceNotFoundException;
import com.gtech.springboot_blog_rest_api.payload.CommentDto;
import com.gtech.springboot_blog_rest_api.repository.CommentRepository;
import com.gtech.springboot_blog_rest_api.repository.PostRepository;
import com.gtech.springboot_blog_rest_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));

        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
//        retrieve comments by postId.
        List<Comment> comments = commentRepository.findByPostId(postId);

//        to convert list of Entities to list of DTOs
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId));

        if(!post.getId().equals(comment.getPost().getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto udpateComment(long postId, long commentId, CommentDto commentRequest) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", postId));

        if (!post.getId().equals(comment.getPost().getId()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not exist in the post");

        Comment newComment = mapToEntity(commentRequest);

        comment.setName(newComment.getName());
        comment.setBody(newComment.getBody());
        comment.setEmail(newComment.getEmail());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);

    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post", "id", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment", "id", postId));

        if (!post.getId().equals(comment.getPost().getId()))
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not exist in the post");

        commentRepository.delete(comment);

    }

    //    entity => DTO : constructor can be used, probably.
    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());

        return commentDto;
    }

//    DTO => entity : we should use proper individual mapping. (OR maybe this could work)
    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment(commentDto.getId(), commentDto.getName(), commentDto.getEmail(), commentDto.getBody() , null);

        return comment;
    }

}
