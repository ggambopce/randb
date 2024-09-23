package com.jinho.randb.domain.post.exception.ex;

public class UnauthorizedPostException extends RuntimeException{
    public  UnauthorizedPostException(String message, Throwable cause) {
        super(message, cause);
    }
}
