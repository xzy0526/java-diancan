package com.qcl.bean;

import com.qcl.meiju.AdminStatusEnum;
import com.qcl.utils.EnumUtil;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 排号对应的数据表bean
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Paihao {
    @Id
    @GeneratedValue
    private Integer id;

    private String day;//那一天的排号
    private Integer num;//当天的号源
    private Integer type;//0小桌，1大桌
    private String openid;//排号人的微信openid
    private Boolean ruzuo;//是否已经叫号
    private String templateid;//小程序订阅消息推送的模板id

    @CreatedDate//自动添加创建时间的注解
    private Date createTime;
    @LastModifiedDate//自动添加更新时间的注解
    private Date updateTime;

}
