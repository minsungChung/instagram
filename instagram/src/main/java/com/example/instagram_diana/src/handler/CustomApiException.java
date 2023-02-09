package com.example.instagram_diana.src.handler;


public class CustomApiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CustomApiException(String message){
        super(message);
    }

}
