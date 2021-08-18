package com.qcl.bean;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
/**
    * @author xzy
    * 评论
*/

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
//@DynamicUpdate
//@DynamicInsert
public class Comment {

    @Id
    @GeneratedValue
    private int commentId;
    private String openid;
    private String name;
    private String avatarUrl;//头像
    private String content;

    @CreatedDate//自动添加创建时间的注解
    private Date createTime;


}
