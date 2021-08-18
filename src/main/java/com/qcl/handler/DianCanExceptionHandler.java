package com.qcl.handler;

import com.qcl.yichang.DianCanAuthorizeException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DianCanExceptionHandler {


    //拦截登录异常
    //http://localhost:8080/diancan/leimu/list
    @ExceptionHandler(value = DianCanAuthorizeException.class)
    public String handlerAuthorizeException() {
        return "zujian/loginView";
    }
}
