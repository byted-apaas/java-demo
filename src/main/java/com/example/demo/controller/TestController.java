package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apaas")
public class TestController {

    @PostMapping("/test1")
    @ResponseBody
    public String testDemo1() {
        return "Hello aPaaS!";
    }

    @PostMapping("/test2")
    @ResponseBody
    public String testDemo2() {
            return "Hello aPaaS!";
        }
}