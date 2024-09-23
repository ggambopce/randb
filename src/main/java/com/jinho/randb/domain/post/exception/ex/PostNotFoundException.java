package com.jinho.randb.domain.post.exception.ex;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message) {
        super(message);
    }
    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
