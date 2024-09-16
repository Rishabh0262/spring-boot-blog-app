package com.gtech.springboot_blog_rest_api.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private long id;
    // name should not be null or empty
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    // email should not be null or empty, email filed validation
    @NotEmpty
    @Email
    private String email;

    // body should not be null or empty, >=10 chars
    @NotEmpty
    @Size(min = 10, message = "Comment body must be minimum 10")
    private String body;
}
