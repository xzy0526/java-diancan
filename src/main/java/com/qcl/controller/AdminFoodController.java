package com.qcl.controller;

import com.qcl.bean.AdminInfo;
import com.qcl.bean.Food;
import com.qcl.bean.Leimu;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.AdminStatusEnum;
import com.qcl.meiju.FoodStatusEnum;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.repository.FoodRepository;
import com.qcl.repository.LeiMuRepository;
import com.qcl.request.FoodReq;
import com.qcl.utils.ExcelExportUtils;
import com.qcl.utils.ExcelImportUtils;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * 餐厅菜品管理
 */
@Controller
@RequestMapping("/food")
@Slf4j
public class AdminFoodController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private LeiMuRepository leiMuRepository;

    //列表
    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                       HttpServletRequest req,
                       ModelMap map) {
        PageRequest request = PageRequest.of(page - 1, size);
        Page<Food> foodPage = foodRepository.findAll(request);
        map.put("foodPage", foodPage);
        map.put("currentPage", page);
        map.put("size", size);
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
        return "food/list";
    }

    //删除某个菜品
    @GetMapping("/remove")
    public String remove(@RequestParam(value = "foodId", required = false) Integer foodId,
                         ModelMap map) {
        foodRepository.deleteById(foodId);
        map.put("url", "/diancan/food/list");
        return "zujian/success";
    }

    //菜品详情页
    @GetMapping("/index")
    public String index(@RequestParam(value = "foodId", required = false) Integer foodId,
                        ModelMap map) {
        if (foodId != null) {
            Food food = foodRepository.findById(foodId).orElse(null);
            map.put("food", food);
        }
        //查询所有的类目
        List<Leimu> leimuList = leiMuRepository.findAll();
        map.put("leimuList", leimuList);
        return "food/index";
    }

    //菜品上架
    @RequestMapping("/on_sale")
    public String onSale(@RequestParam("foodId") int foodId,
                         ModelMap map) {
        try {
            Food food = foodRepository.findById(foodId).orElse(null);
            if (food == null) {
                throw new DianCanException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            if (food.getFoodStatusEnum() == FoodStatusEnum.UP) {
                throw new DianCanException(ResultEnum.PRODUCT_STATUS_ERROR);
            }
            food.setFoodStatus(FoodStatusEnum.UP.getCode());
            foodRepository.save(food);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/food/list");
            return "zujian/error";
        }

        map.put("url", "/diancan/food/list");
        return "zujian/success";
    }

    //菜品下架
    @RequestMapping("/off_sale")
    public String offSale(@RequestParam("foodId") int foodId,
                          ModelMap map) {
        try {
            Food food = foodRepository.findById(foodId).orElse(null);
            if (food == null) {
                throw new DianCanException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            if (food.getFoodStatusEnum() == FoodStatusEnum.DOWN) {
                throw new DianCanException(ResultEnum.PRODUCT_STATUS_ERROR);
            }
            food.setFoodStatus(FoodStatusEnum.DOWN.getCode());
            foodRepository.save(food);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/food/list");
            return "zujian/error";
        }

        map.put("url", "/diancan/food/list");
        return "zujian/success";
    }


    //菜品添加或更新
    @PostMapping("/save")
    public String save(@Valid FoodReq form,
                       BindingResult bindingResult,
                       ModelMap map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/food/index");
            return "zujian/error";
        }

        Food productInfo = new Food();
        try {
            //如果productId为空, 说明是新增
            if (!StringUtils.isEmpty(form.getFoodId())) {
                productInfo = foodRepository.findById(form.getFoodId()).orElse(null);
            }
            BeanUtils.copyProperties(form, productInfo);
            foodRepository.save(productInfo);
        } catch (Exception e) {
            log.error("添加菜品错误={}", e);
            map.put("msg", "添加菜品出错");
            map.put("url", "/diancan/food/index");
            return "zujian/error";
        }

        map.put("url", "/diancan/food/list");
        return "zujian/success";
    }


    //导出菜品到excel
    @GetMapping("/export")
    public String export(HttpServletResponse response, ModelMap map) {
        String fileName = "菜品商品导出";
        String[] titles = {"菜品名", "单价", "库存", "类目", "描述", "商品图片"};
        List<Food> foodList = foodRepository.findAll();
        if (foodList == null || foodList.size() < 1) {
            map.put("msg", "菜品为空");
            map.put("url", "/diancan/food/list");
            return "zujian/error";
        }
        int size = foodList.size();
        String[][] dataList = new String[size][titles.length];
        for (int i = 0; i < size; i++) {
            Food food = foodList.get(i);
            dataList[i][0] = food.getFoodName();
            dataList[i][1] = "" + food.getFoodPrice();
            dataList[i][2] = "" + food.getFoodStock();//库存
            dataList[i][3] = "" + food.getLeimuType();//菜品类目的type
            dataList[i][4] = food.getFoodDesc();
            dataList[i][5] = food.getFoodIcon();
        }

        try {
            ExcelExportUtils.createWorkbook(fileName, titles, dataList, response);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "导出excel失败");
            map.put("url", "/diancan/food/list");
            return "zujian/error";
        }
        map.put("url", "/diancan/food/list");
        return "zujian/success";
    }

    //excel导入网页
    @GetMapping("/excel")
    public String excel(ModelMap map) {
        return "food/excel";
    }

    /*
     * 批量导入excel里的菜品(商品)到数据库
     * */
    @RequestMapping("/uploadExcel")
    public String uploadExcel(@RequestParam("file") MultipartFile file,
                              ModelMap map) {
        String name = file.getOriginalFilename();
        if (name.length() < 6 || !name.substring(name.length() - 5).equals(".xlsx")) {
            map.put("msg", "文件格式错误");
            map.put("url", "/diancan/food/excel");
            return "zujian/error";
        }
        List<Food> list;
        try {
            list = ExcelImportUtils.excelToFoodInfoList(file.getInputStream());
            log.info("excel导入的list={}", list);
            if (list == null || list.size() <= 0) {
                map.put("msg", "导入失败");
                map.put("url", "/diancan/food/excel");
                return "zujian/error";
            }
            //excel的数据保存到数据库
            try {
                for (Food excel : list) {
                    if (excel != null) {
                        foodRepository.save(excel);
                    }
                }
            } catch (Exception e) {
                log.error("某一行存入数据库失败={}", e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/food/excel");
            return "zujian/error";
        }
        map.put("url", "/diancan/food/list");
        return "zujian/success";
    }


}
