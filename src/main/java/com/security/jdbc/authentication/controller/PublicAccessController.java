package com.security.jdbc.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicAccessController {


    @GetMapping("/")
    public String getString(){
        return "Hello from the public API!";
    }
}
