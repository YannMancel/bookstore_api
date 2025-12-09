package com.mancel.yann.bookstore_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.method.HandlerMethod;

import java.io.UnsupportedEncodingException;

public abstract class MvcResultTools {

    public static boolean isMethodName(MvcResult mvcResult, String name) {
        var handler = (HandlerMethod) mvcResult.getHandler();
        if (handler == null) throw new IllegalStateException("Handler is null.");
        return handler.getMethod().getName().equals(name);
    }

    public static boolean isStatus(MvcResult mvcResult, HttpStatus status) {
        var response = mvcResult.getResponse();
        return response.getStatus() == status.value();
    }

    public static <T> boolean hasContent(MvcResult mvcResult, ObjectMapper objectMapper, T object) {
        var response = mvcResult.getResponse();
        try {
            return response.getContentAsString().equals(objectMapper.writeValueAsString(object));
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasException(MvcResult mvcResult, Exception expectedException) {
        var exception = mvcResult.getResolvedException();
        if (exception == null) throw new IllegalStateException("Exception is null.");
        return exception.equals(expectedException);
    }
}
