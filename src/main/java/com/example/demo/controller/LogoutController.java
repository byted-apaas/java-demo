package com.example.demo.controller;

import com.example.demo.log.Logger;
import com.example.demo.log.annotation.RequestLogger;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class LogoutController {
    static class Params {
        public String name;
        public String token;
    }

    @JsonFormat
    static class Result {
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public Result logout(@RequestBody LoginController.Params params, @RequestHeader Map<String, String> headers, @RequestLogger Logger logger) {
        // 日志功能
        logger.info("%s 退出登陆", params.name);

        // 在这里补充业务代码

        return new Result();
    }
}
