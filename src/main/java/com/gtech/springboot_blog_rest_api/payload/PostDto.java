package com.gtech.springboot_blog_rest_api.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private Long id;

    // title should not be null or empty, >=2 chars
    @NotEmpty
    @Size(min = 2, message = " Post title should have at least 2 characters")
    private String title;

    // title should not be null or empty, >=10 chars
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;

    @NotEmpty
    private String content;

    private Set<CommentDto> comments;

}
