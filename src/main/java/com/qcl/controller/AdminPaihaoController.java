package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.Food;
import com.qcl.bean.Paihao;
import com.qcl.bean.PictureInfo;
import com.qcl.meiju.FoodStatusEnum;
import com.qcl.meiju.ResultEnum;
import com.qcl.push.SendWxMessage;
import com.qcl.repository.PaihaoRepository;
import com.qcl.repository.PictureRepository;
import com.qcl.request.PictureForm;
import com.qcl.utils.ApiUtil;
import com.qcl.utils.TimeUtils;
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
 * 排号相关
 */
@Controller
@RequestMapping("/adminPaihao")
@Slf4j
public class AdminPaihaoController {

    @Autowired
    PaihaoRepository repository;
    @Autowired
    SendWxMessage wxSend;

    /*
     * 页面相关
     * */
    @GetMapping("/list")
    public String list(ModelMap map) {
        //获取小桌未入座的用户
        List<Paihao> listSmall = repository.findByDayAndRuzuoAndTypeOrderByNum(TimeUtils.getYMD(), false, 0);
        //获取大桌未入座的用户
        List<Paihao> listBig = repository.findByDayAndRuzuoAndTypeOrderByNum(TimeUtils.getYMD(), false, 1);
        map.put("listSmall", listSmall);
        map.put("listBig", listBig);
        return "paihao/list";
    }

    @GetMapping("/ruzhuo")
    public String ruzhuo(@RequestParam("id") int id, ModelMap map) {
        try {
            Paihao paihao = repository.findById(id).orElse(null);
            if (paihao == null) {
                throw new DianCanException(ResultEnum.PAIHAO_NOT_EXIST);
            }
            if (paihao.getRuzuo()) {
                throw new DianCanException(ResultEnum.PAIHAO_STATUS_ERROR);
            }
            paihao.setRuzuo(true);
            repository.save(paihao);
            //发送订阅消息给当前排号用户
            wxSend.pushOneUser(id);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/adminPaihao/list");
            return "zujian/error";
        }

        map.put("url", "/diancan/adminPaihao/list");
        return "zujian/success";
    }
}
