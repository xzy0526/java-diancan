package com.qcl.push;

import lombok.Data;

/*
 * 用来封装订阅消息内容
 * */
@Data
public class TemplateData {
    private String value;//

    public TemplateData(String value) {
        this.value = value;
    }

}