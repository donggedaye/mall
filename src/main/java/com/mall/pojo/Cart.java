package com.mall.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "mmall_cart")
@Data
public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;


    public Cart() {

    }
}