package com.mall.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "mmall_payinfo")
@Data
public class PayInfo {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

    private Date createTime;

    private Date updateTime;

    public PayInfo() {

    }
}