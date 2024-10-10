package com.example.demo.controller;

import com.example.demo.log.Logger;
import com.example.demo.log.annotation.RequestLogger;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PrintContext {

    static class Params {
        public Boolean bPrintEnvVar = true;
        public Boolean bPrintContext = true;
    }

    @JsonFormat
    static class Result {
    }


    /**
     * @apiName {string}    print_info
     * @labelCN {string}    打印上下文
     * @labelEN {string}    print context information
     * @descriptionCN {string}  从 header 中获取云函数运行的上下文信息，包括：触发函数的上下文、租户信息、应用信息等
     * @descriptionEN {string}  get context information from header, and print by log
     * @frontendSDKInvokable {bool}   true
     */
    @PostMapping("/printInfo")
    @ResponseBody
    public Result PrintInfo(@RequestBody Params params, @RequestHeader Map<String, String> headers, @RequestLogger Logger logger) {
        logger.info("开始打印上下文：");
        logger.info("Trigger Type: {}",headers.get("x-kunlun-trigger-type"));
        logger.info("Trigger Info: {}",headers.get("x-apaas-persist-faas-request-source"));
        logger.info("Context: {}",headers.get("x-kunlun-tenant"));

        return new Result();
    }
}
