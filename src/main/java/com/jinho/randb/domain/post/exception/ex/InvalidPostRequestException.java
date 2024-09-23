package com.jinho.randb.domain.post.exception.ex;

public class InvalidPostRequestException extends RuntimeException {
    public InvalidPostRequestException (String message, Throwable cause) {
        super(message, cause);
    }
}
