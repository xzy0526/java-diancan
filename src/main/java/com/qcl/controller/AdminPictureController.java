package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.AdminInfo;
import com.qcl.bean.PictureInfo;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.AdminStatusEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.yichang.DianCanException;
import com.qcl.request.PictureForm;
import com.qcl.repository.PictureRepository;
import com.qcl.utils.ApiUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户相关
 */
@Controller
@RequestMapping("/picture")
@Slf4j
public class AdminPictureController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    PictureRepository repository;

    /*
     * 页面相关
     * */
    @GetMapping("/list")
    public String list(HttpServletRequest req, ModelMap map) {
        List<PictureInfo> pictures = repository.findAll();
        map.put("categoryList", pictures);
        // 校验是管理员还是员工
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(GlobalConst.COOKIE_TOKEN)) {
                    String cookieValue = cookie.getValue();
                    log.info("获取到存储的cookie={}", cookieValue);
                    if (!StringUtils.isEmpty(cookieValue)) {
                        AdminInfo adminInfo = adminRepository.findByAdminId(Integer.parseInt(cookieValue));
                        if (adminInfo != null && Objects.equals(AdminStatusEnum.SUPER_ADMIN.getCode(), adminInfo.getAdminType())) {
                            map.put("isAdmin", true);
                        }
                    }
                }
            }
        }
        return "picture/list";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "picId", required = false) Integer picId,
                        ModelMap map) {
        PictureInfo picture = repository.findByPicId(picId);
        map.put("category", picture);
        return "picture/index";
    }

    //删除轮播图
    @GetMapping("/remove")
    public String remove(@RequestParam(value = "picId", required = false) Integer picId,
                         ModelMap map) {
        repository.deleteById(picId);
        map.put("url", "/diancan/picture/list");
        return "zujian/success";
    }

    //保存/更新
    @PostMapping("/save")
    public String save(@Valid PictureForm form,
                       BindingResult bindingResult,
                       ModelMap map) {
        log.info("SellerForm={}", form);
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/picture/index");
            return "zujian/error";
        }
        PictureInfo picture = new PictureInfo();
        try {
            if (form.getPicId() != null) {
                picture = repository.findByPicId(form.getPicId());
            }
            BeanUtils.copyProperties(form, picture);
            repository.save(picture);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/picture/index");
            return "zujian/error";
        }

        map.put("url", "/diancan/picture/list");
        return "zujian/success";
    }


    /*
     * 返回json给小程序
     * */
    @GetMapping("/getAll")
    @ResponseBody
    public ResultVO getUserInfo() {
        List<PictureInfo> pictures = repository.findAll();
        return ApiUtil.success(pictures);
    }

}
