package com.qcl.bean;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 用户信息表
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {

    @Id
    private String openid;//用openid作为主键

    private String username;
    private String phone;
    private String zhuohao;//桌号
    private String renshu;//用餐人数
    private BigDecimal money;//余额
    private Long viptime;//会员结束时间

    @CreatedDate//自动添加创建时间的注解
    private Date createTime;
    @LastModifiedDate//自动添加更新时间的注解
    private Date updateTime;
}
