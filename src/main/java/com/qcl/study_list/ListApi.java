package com.qcl.study_list;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ListApi {

    /*
     * 返回列表数据给前端
     * */
    @GetMapping("/getList")
    public List getList() {
        List<String> list = new ArrayList<>();
        list.add("编程小石头1");
        list.add("编程小石头2");
        list.add("编程小石头3");
        return list;
    }
}
