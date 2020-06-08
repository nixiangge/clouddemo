package com.guoyu.elk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
//全局捕获异常
@ControllerAdvice
public class MyExceptionHand {
    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {

        return e.getMessage();
    }
    //请求404
    //yml配置：spring.mvc.throw-exception-if-no-handler-found=true
    //spring.resources.add-mappings=false
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFountHandler(HttpServletRequest request, NoHandlerFoundException e) {
        return "index404";
    }
}
