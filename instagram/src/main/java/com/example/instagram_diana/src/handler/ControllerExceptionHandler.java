package com.example.instagram_diana.src.handler;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public CustomResponseDto<?> validationException(CustomValidationException e){
        return new CustomResponseDto<Map<String,String>>(-1,e.getMessage(),e.getErrorMap());

    }


}
