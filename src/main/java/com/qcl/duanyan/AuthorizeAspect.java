package com.qcl.duanyan;

import com.qcl.global.GlobalConst;
import com.qcl.meiju.ResultEnum;
import com.qcl.yichang.DianCanAuthorizeException;
import com.qcl.utils.CookieUtil;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AuthorizeAspect {


    //    @Pointcut("execution(public * com.qcl.controller.Seller*.*(..))" +
    //    "&& !execution(public * com.qcl.controller.SellerUserController.*(..))")
    @Pointcut("execution(public * com.qcl.controller.Admin*.*(..))")
    public void verify() {
    }

    //查询cookie
    @Before("verify()")
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtil.get(request, GlobalConst.COOKIE_TOKEN);
        if (cookie == null) {
            log.warn("Cookie中查不到token");
            throw new DianCanAuthorizeException();
        }
    }
}
