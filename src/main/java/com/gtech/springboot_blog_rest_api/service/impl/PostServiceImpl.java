package com.gtech.springboot_blog_rest_api.service.impl;

import com.gtech.springboot_blog_rest_api.entity.Post;
import com.gtech.springboot_blog_rest_api.exception.ResourceNotFoundException;
import com.gtech.springboot_blog_rest_api.payload.PostDto;
import com.gtech.springboot_blog_rest_api.repository.PostRepository;
import com.gtech.springboot_blog_rest_api.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

//        convert DTO to entity
        Post post = mapToEntity(postDto);
//        Post post = new Post();
//
//        post.setId(postDto.getId());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        Post newPost = postRepository.save(post);

//        convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);
//        PostDto postResponse = new PostDto();
//
//        postResponse.setId(newPost.getId());
//        postResponse.setTitle(newPost.getTitle());
//        postResponse.setContent(newPost.getContent());
//        postResponse.setDescription(newPost.getDescription());

        return postResponse;
    }

    @Override
    public List<PostDto> getAllPosts() {

        List<Post> posts = postRepository.findAll();

        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());     // lamda expression
//        return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostByID(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    //    Method to convert entity to DTO
    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();

        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        return post;
    }

//    Method to convert DTO to entity
    private PostDto mapToDTO(Post post) {
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());

        return postDto;
    }



}
