package com.gtech.springboot_blog_rest_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class BlogAPIException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public BlogAPIException(HttpStatus status, String message) {
        this.message = message;
        this.status = status;
    }

    public BlogAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.message = message1;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
