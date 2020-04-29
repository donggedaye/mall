package com.mall.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "mmall_category")
@Data
public class Category {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    public Category() {

    }

}