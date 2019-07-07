package com.nuadu.rest.exception;

public class FileNotFoundException extends BaseNotFoundException {

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
