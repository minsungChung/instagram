package com.example.instagram_diana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController2 {

    @GetMapping ("/testview")
    public String testtt(){
        return "index";

    }
}
