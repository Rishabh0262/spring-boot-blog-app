package com.gtech.springboot_blog_rest_api.controller;


import com.gtech.springboot_blog_rest_api.payload.CommentDto;
import com.gtech.springboot_blog_rest_api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")             // we can use ("/api/posts/{postId}/comments") , as common to all the REST-API.
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId,
                                                    @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentByPostId(@PathVariable Long postId) {
        return  commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public  CommentDto getCommentById(@PathVariable Long postId, @PathVariable(value = "id") Long commentId ){

        return commentService.getCommentById(postId, commentId);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public CommentDto updateComment(@PathVariable long postId,
                                    @PathVariable(value = "id") long commentId,
                                    @RequestBody CommentDto comment){
        return commentService.udpateComment(postId,commentId, comment);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable long postId,
                                                @PathVariable(value = "id") long commentId
                                                ) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
