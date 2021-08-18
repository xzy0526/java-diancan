package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.PictureInfo;
import com.qcl.repository.PictureRepository;
import com.qcl.request.PictureForm;
import com.qcl.utils.ApiUtil;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 小程序端轮播图
 */
@RestController
@RequestMapping("/wxPicture")
public class WxPictureController {
    @Autowired
    PictureRepository repository;

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
