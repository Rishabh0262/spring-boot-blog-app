package com.gtech.springboot_blog_rest_api.service;

import com.gtech.springboot_blog_rest_api.payload.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts();

    PostDto getPostByID(long id);
}
