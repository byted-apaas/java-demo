package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apaas")
public class TestController1 {

    @RequestMapping("/test1")
    @ResponseBody
    public String testDemo1() {
        return "Hello aPaaS!";
    }

     @RequestMapping("/test2")
        @ResponseBody
        public String testDemo2() {
            return "Hello aPaaS!";
        }
}