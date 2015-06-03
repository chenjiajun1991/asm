package com.sam.yh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @RequestMapping("/hello")
    public String hello1(String[] param1, String param2) {
        return "Hello" + param1[0] + param1[1] + param2;
    }
}
