package com.qcl.controller;

import com.qcl.bean.AdminInfo;
import com.qcl.bean.Comment;
import com.qcl.bean.Food;
import com.qcl.bean.Leimu;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.AdminStatusEnum;
import com.qcl.meiju.FoodStatusEnum;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.repository.CommentRepository;
import com.qcl.repository.FoodRepository;
import com.qcl.repository.LeiMuRepository;
import com.qcl.request.FoodReq;
import com.qcl.utils.ExcelExportUtils;
import com.qcl.utils.ExcelImportUtils;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 用户评价
 */
@Controller
@RequestMapping("/comment")
@Slf4j
public class AdminCommentController {

    @Autowired
    private CommentRepository commentRepository;


    //列表
    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       ModelMap map) {
        PageRequest request = PageRequest.of(page - 1, size);
        Page<Comment> commentPage = commentRepository.findAll(request);
        map.put("commentPage", commentPage);
        map.put("currentPage", page);
        map.put("size", size);
        return "comment/list";
    }


}
