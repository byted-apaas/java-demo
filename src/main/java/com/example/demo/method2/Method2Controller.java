package com.example.demo.method2;


import com.example.demo.log.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


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

    public Result method2(@RequestBody Params params, @RequestHeader Map<String, String> headers) { //  @RequestLogger Logger logger
        Logger logger = new Logger(headers);
        logger.info("%s 函数开始执行", new Date());
        Result res = new Result();
        res.name = params.name;
        res.password = params.password;
        res.rowBody = "Hello aPaaS!, from method2";
        return res;
    }
}
