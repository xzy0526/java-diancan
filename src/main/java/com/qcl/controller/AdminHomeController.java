package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.AdminInfo;
import com.qcl.bean.Food;
import com.qcl.bean.TotalMoney;
import com.qcl.bean.WxOrderRoot;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.AdminStatusEnum;
import com.qcl.meiju.OrderStatusEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.repository.FoodRepository;
import com.qcl.repository.OrderRootRepository;
import com.qcl.utils.ApiUtil;
import com.qcl.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * 点餐后台首页
 * */
@Controller
@RequestMapping("/home")
@Slf4j
public class AdminHomeController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    OrderRootRepository orderRootRepository;
    @Autowired
    FoodRepository foodRepository;

    /*
     * 页面相关
     * 1,查询当月收入
     * 2，库存预警
     * 3，账单查询
     * */
    @GetMapping("/homeList")
    public String homeList(HttpServletRequest req, ModelMap map) {
        int year = TimeUtils.getCurrentYear();
        int month = TimeUtils.getCurrentMonth();
        List<TotalMoney> totalMoneyList = new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            TotalMoney totalMoney = new TotalMoney();
            totalMoney.setTime(year + "年" + i + "月");
            totalMoney.setTotalMoney(getMonthMoney(year, i));
            totalMoneyList.add(totalMoney);
        }
        map.put("totalMoneyList", totalMoneyList);
        map.put("yearMoney", getYearMoney(year));
        map.put("foodList", getFoodKuCunList());
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
        return "home/list";
    }

    //获取今年年收入
    private BigDecimal getYearMoney(int year) {
        Specification<WxOrderRoot> spec1 = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            try {
                Integer[] statusArr = {
                        OrderStatusEnum.NEW_PAYED.getCode(),
                        OrderStatusEnum.FINISHED.getCode(),
                        OrderStatusEnum.COMMENT.getCode()
                };
                list.add(root.get("orderStatus").in(statusArr));
                //大于或等于传入时间
                list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getFisrtDayOfMonth(year, 1)));
                //小于或等于传入时间
                list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getLastDayOfMonth(year, 12)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        List<WxOrderRoot> orderList = orderRootRepository.findAll(spec1);
        BigDecimal totalMoney = new BigDecimal(0);
        for (WxOrderRoot root : orderList) {
            totalMoney = totalMoney.add(root.getOrderAmount());
        }
        return totalMoney;
    }

    //获取每个月份的收入
    private BigDecimal getMonthMoney(int year, int month) {
        //查询当月订单
        Specification<WxOrderRoot> spec1 = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            try {
                Integer[] statusArr = {
                        OrderStatusEnum.NEW_PAYED.getCode(),
                        OrderStatusEnum.FINISHED.getCode(),
                        OrderStatusEnum.COMMENT.getCode()
                };
                list.add(root.get("orderStatus").in(statusArr));
                //大于或等于传入时间
                list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getFisrtDayOfMonth(year, month)));
                //小于或等于传入时间
                list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getLastDayOfMonth(year, month)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        List<WxOrderRoot> orderList = orderRootRepository.findAll(spec1);
        BigDecimal totalMoney = new BigDecimal(0);
        for (WxOrderRoot root : orderList) {
            totalMoney = totalMoney.add(root.getOrderAmount());
        }
//        log.error("查询到指定月份的订单列表={}", orderList);
        return totalMoney;
    }

    //查询库存少于5的菜品，提醒管理员及时补充库存
    private List<Food> getFoodKuCunList() {
        return foodRepository.findByFoodStockLessThan(5);
    }
}
