package com.gtech.springboot_blog_rest_api.service.impl;

import com.gtech.springboot_blog_rest_api.entity.Post;
import com.gtech.springboot_blog_rest_api.exception.ResourceNotFoundException;
import com.gtech.springboot_blog_rest_api.payload.PostDto;
import com.gtech.springboot_blog_rest_api.payload.PostResponse;
import com.gtech.springboot_blog_rest_api.repository.PostRepository;
import com.gtech.springboot_blog_rest_api.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
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
    public List<PostDto> getAllPosts1(int pageNo, int pageSize) {

//        create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize);

//        List<Post> posts = postRepository.findAll(pageable);      // previously #1
        Page<Post> posts = postRepository.findAll(pageable);

//        get content for page object
        List<Post> listOfPosts = posts.getContent();

//        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());     // lamda expression... for previously #1

        return listOfPosts.stream().map(this::mapToDTO).collect(Collectors.toList());     // lamda expression... for using Pageable object of Pagination
//        return listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

    }



    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
//      create a sort obj. to pass it in
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

//        create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

//        get content for page object
        List<Post> listOfPosts = posts.getContent();


//        ************* Customizing pagination API res.
//        all the data of "postResponse" to be mapped from "posts" of slice.class->Page.class
        List<PostDto> content = listOfPosts.stream().map(this::mapToDTO).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostByID(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);

    }


    //    Method to convert entity to DTO
    private Post mapToEntity(PostDto postDto) {

        Post post = mapper.map(postDto, Post.class);

//        Post post = new Post();
//
//        post.setId(postDto.getId());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        return post;
    }

//    Method to convert DTO to entity
    private PostDto mapToDTO(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
        
//        PostDto postDto = new PostDto();
//
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());

        return postDto;
    }



}
