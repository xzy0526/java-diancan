package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.UserInfo;
import com.qcl.meiju.ResultEnum;
import com.qcl.yichang.DianCanException;
import com.qcl.request.UserForm;
import com.qcl.repository.UserRepository;
import com.qcl.utils.ApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户相关
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class WxUserController {

    @Autowired
    UserRepository repository;

    //用户注册或者修改信息
    @PostMapping("/save")
    public ResultVO create(@Valid UserForm userForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确, userForm={}", userForm);
            throw new DianCanException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        log.error("参数, userForm={}", userForm);

        UserInfo user = repository.findByOpenid(userForm.getOpenid());
        Float money = 0f;
        if (user != null) {
            money = money + userForm.getMoney();
        } else {//第一次注册
            user = new UserInfo();
            money = userForm.getMoney();
            user.setViptime(System.currentTimeMillis());
        }
        user.setUsername(userForm.getUsername());
        user.setOpenid(userForm.getOpenid());
        user.setPhone(userForm.getPhone());
        user.setZhuohao(userForm.getZhuohao());
        user.setRenshu(userForm.getRenshu());
        if(money>0f){
            user.setMoney(new BigDecimal(Float.toString(money)));
        }

        return ApiUtil.success(repository.save(user));
    }

    //用户注册或者修改信息
    @PostMapping("/payVip")
    public ResultVO payVip(@RequestParam("openid") String openid,
                           @RequestParam("time") Long time,
                           @RequestParam("money") Integer money) {


        UserInfo user = repository.findByOpenid(openid);

        user.setOpenid(openid);
        BigDecimal totalFeeDecimal = user.getMoney();
        BigDecimal totalFeeAfterDecimal = new BigDecimal(
                Float.toString(money));
        BigDecimal result = totalFeeDecimal.subtract(
                totalFeeAfterDecimal);

        user.setMoney(result);//充值会员扣积分
        user.setViptime(user.getViptime() + time);//增加会员时间

        return ApiUtil.success(repository.save(user));
    }

    @GetMapping("/getUserInfo")
    public ResultVO getUserInfo(@RequestParam("openid") String openid) {
        UserInfo user = repository.findByOpenid(openid);
        return ApiUtil.success(user);
    }

}
