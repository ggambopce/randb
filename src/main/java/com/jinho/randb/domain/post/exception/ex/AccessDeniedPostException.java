package com.jinho.randb.domain.post.exception.ex;

public class AccessDeniedPostException extends RuntimeException{
    public AccessDeniedPostException(String message, Throwable cause) {
        super(message, cause);
    }
}
