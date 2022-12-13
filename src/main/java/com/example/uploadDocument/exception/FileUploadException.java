package com.example.uploadDocument.exception;


public class FileUploadException extends RuntimeException {

    protected int status;

    public FileUploadException(int status, String message) {
        super(message);
        this.status = status;
    }

}
