package com.gtech.springboot_blog_rest_api.payload;

import lombok.Data;

@Data
public class CommentDto {
    private long id;
    private String name, email, body;
}
