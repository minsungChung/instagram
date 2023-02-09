package com.example.instagram_diana.src.handler;


import java.util.Map;

// 유효성 ex
public class CustomValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private Map<String,String> errorMap;

    public CustomValidationException(String message,Map<String,String> errorMap){
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String,String> getErrorMap(){
        return errorMap;
    }

}
