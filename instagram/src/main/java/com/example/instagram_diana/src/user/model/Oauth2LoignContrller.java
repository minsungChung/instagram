package com.example.instagram_diana.src.user.model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Oauth2LoignContrller {

    @GetMapping("/oauth2loginview")
    public String Oauth2Loign(){
        return "oauth2BeforeLogin";
    }
}


