package com.gtech.springboot_blog_rest_api.service;

import com.gtech.springboot_blog_rest_api.payload.PostDto;
import com.gtech.springboot_blog_rest_api.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts1(int pageNo, int pageSize);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostByID(long id);

    PostDto updatePost(PostDto postDto ,long id);

    void deletePost(long id);
}
