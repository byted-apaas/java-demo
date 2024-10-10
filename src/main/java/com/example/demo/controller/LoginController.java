package com.example.demo.controller;

import com.example.demo.log.annotation.RequestLogger;
import com.example.demo.log.Logger;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class LoginController {

    static class Params {
        public String name;
        public String password;

        @Override
        public String toString() { // 用于输出格式化日志
            return String.format("name: %s, password: %s", name, password);
        }
    }

    @JsonFormat
    static class Result {
        public String message;
        public String token;
    }


    /**
     * @apiName {string}    log_in
     * @labelCN {string}    登录
     * @labelEN {string}    log in account
     * @descriptionCN {string}  登录账号
     * @descriptionEN {string}  log_out an account
     * @frontendSDKInvokable {bool}   true
     */
    @PostMapping("/user/login")
    @ResponseBody
    public Result login(@RequestBody Params params, @RequestHeader Map<String, String> headers, @RequestLogger Logger logger) {
        logger.info("{} 开始登陆, params: {},\n header: {} ", params.name, params, headers);

        Result res = new Result();
        res.message = String.format("user: %s, login success.", params.name);
        res.token = md5(String.format("%s %s", params.name, params.password));
        return res;
    }

    public String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
