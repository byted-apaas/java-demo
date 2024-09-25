package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import com.example.demo.log.resolver.RequestLoggerResolver;

import java.util.List;

@SpringBootApplication
public class DemoApplication extends WebMvcConfigurationSupport {
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestLoggerResolver());
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
