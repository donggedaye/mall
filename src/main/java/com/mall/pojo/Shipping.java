package com.mall.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "mmall_shipping")
@Data
public class Shipping {
    private Integer id;

    private Integer userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;

    private Date updateTime;

    public Shipping() {

    }
}