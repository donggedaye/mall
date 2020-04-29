package com.mall.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "mmall_user")
@Data
public class User {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;


    public User() {

    }
}