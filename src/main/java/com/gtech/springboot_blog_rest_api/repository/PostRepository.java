package com.gtech.springboot_blog_rest_api.repository;

import com.gtech.springboot_blog_rest_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
//    We don't need to write any code here.
    // JpaRepository<Post, Long> impicitly has all the required code


}
