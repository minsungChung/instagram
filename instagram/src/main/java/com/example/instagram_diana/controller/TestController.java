package com.example.instagram_diana.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testapi")
public class TestController {

    @GetMapping("/testget")
    public @ResponseBody String testget(){
        return "Hello! Get Success.";

    }

    @ResponseBody
    @PostMapping("/test/{useridx}")
    public String testpost(@PathVariable int useridx){
        return "Hello! Post Success:"+useridx;

    }


    @ResponseBody
    @PostMapping("/test/name/{testname}")
    public String testpostname(@PathVariable String testname){
        return "Hello! Post Success:"+ testname;

    }

    @PatchMapping("/testpatch")
    public String testttt(){
        return "Hello! Patch Success.";

    }

    @GetMapping("/oauth2")
    public @ResponseBody String testget1(){
        return "oauth2";

    }

    @GetMapping("login/oauth2/code/facebook")
    public @ResponseBody String testget2(){
        return "login/oauth2...";

    }



}