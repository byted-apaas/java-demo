package com.example.demo.log.resolver;

import com.example.demo.log.annotation.RequestLogger;
import com.example.demo.log.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestLoggerResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestLogger.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        final RequestLogger parameterAnnotation = parameter.getParameterAnnotation(RequestLogger.class);
        if (parameterAnnotation == null) {
            return null;
        }

        Map<String, String> headers = new LinkedHashMap();
        Iterator iterator = webRequest.getHeaderNames();

        while (iterator.hasNext()) {
            String headerName = (String) iterator.next();
            String headerValue = webRequest.getHeader(headerName);
            if (headerValue != null) {
                headers.put(headerName, headerValue);
            }
        }

        Logger logger = new Logger(headers);
        return logger;
    }
}
