package com.example.demo.method2;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class Method2Controller {
    public static class Params {
        public String name;
        public String password;
    }

    public static class Result {
        public String name;
        public String password;
        public String rowBody;
    }

    @PostMapping("/method2")
    @ResponseBody
    public Result method2(@RequestBody Params params) {
        Result res = new Result();
        res.name = params.name;
        res.password = params.password;
        res.rowBody = "Hello aPaaS!, from method2";
        return res;
    }
}
