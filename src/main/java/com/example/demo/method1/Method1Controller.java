package com.example.demo.method1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Method1Controller {
    @PostMapping("/method1")
    @ResponseBody
    public String method1() {
        return "Hello aPaaS!, from method1";
    }
}
