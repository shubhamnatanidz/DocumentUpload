package com.example.uploadDocument.exception;


public class FileUploadException extends RuntimeException {

    protected int status;

    public FileUploadException(int status) {
        super();
        this.status = status;
    }

    public FileUploadException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public FileUploadException(int status, String message) {
        super(message);
        this.status = status;
    }

    public FileUploadException(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }


}
