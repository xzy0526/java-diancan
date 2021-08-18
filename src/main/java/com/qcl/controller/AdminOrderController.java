package com.qcl.controller;

import com.qcl.bean.AdminInfo;
import com.qcl.bean.Food;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.AdminStatusEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.repository.OrderRootRepository;
import com.qcl.response.WxOrderResponse;
import com.qcl.meiju.ResultEnum;
import com.qcl.utils.ExcelExportUtils;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 卖家端订单页
 */
@Controller
@RequestMapping("/adimOrder")
@Slf4j
public class AdminOrderController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private WxOrderUtils wxOrder;

    //订单列表
    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "20") Integer size,
                       ModelMap map) {
        //最新的订单在最前面
        PageRequest request = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        Page<WxOrderResponse> orderDTOPage = wxOrder.findList(request);
        log.error("后台显示的订单列表={}", orderDTOPage.getTotalElements());
        log.error("后台显示的订单列表={}", orderDTOPage.getContent());
        map.put("orderDTOPage", orderDTOPage);
        map.put("currentPage", page);
        map.put("size", size);
        return "order/list";
    }

    //只有取消的订单才可以删除
    @GetMapping("/remove")
    public String remove(@RequestParam(value = "orderId", required = false) Integer orderId,
                         ModelMap map) {
        wxOrder.remove(orderId);
        map.put("url", "/diancan/adimOrder/list");
        return "zujian/success";
    }

    //取消订单
    @GetMapping("/cancel")
    public String cancel(@RequestParam("orderId") int orderId,
                         ModelMap map) {
        try {
            WxOrderResponse orderDTO = wxOrder.findOne(orderId);
            wxOrder.cancel(orderDTO);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/adimOrder/list");
            return "zujian/error";
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url", "/diancan/adimOrder/list");
        return "zujian/success";
    }

    //退菜，把某个菜品订单删除，营业额里相应减除金额
    @GetMapping("/tuicai")
    public String tuicai(@RequestParam("orderId") int orderId,
                         @RequestParam("detailId") int detailId,
                         ModelMap map) {
        try {
            wxOrder.tuicai(orderId, detailId);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/adimOrder/list");
            return "zujian/error";
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url", "/diancan/adimOrder/list");
        return "zujian/success";
    }

    //订单详情
    @GetMapping("/detail")
    public String detail(@RequestParam("orderId") int orderId,
                         HttpServletRequest req,
                         ModelMap map) {
        WxOrderResponse orderDTO = new WxOrderResponse();
        try {
            orderDTO = wxOrder.findOne(orderId);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/adimOrder/list");
            return "zujian/error";
        }

        map.put("orderDTO", orderDTO);
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
        return "order/detail";
    }

    //上菜完成订单
    @GetMapping("/finish")
    public String finished(@RequestParam("orderId") int orderId,
                           ModelMap map) {
        try {
            WxOrderResponse orderDTO = wxOrder.findOne(orderId);
            wxOrder.finish(orderDTO);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/adimOrder/list");
            return "zujian/error";
        }

        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        map.put("url", "/diancan/adimOrder/list");
        return "zujian/success";
    }

    //导出菜品订单到excel
    @GetMapping("/export")
    public String export(HttpServletResponse response, ModelMap map) {
        try {
            wxOrder.exportOrderToExcel(response);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "导出excel失败");
            map.put("url", "/diancan/adimOrder/list");
            return "zujian/error";
        }
        map.put("url", "/diancan/adimOrder/list");
        return "zujian/success";
    }

}
