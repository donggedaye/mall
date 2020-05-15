package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUservice iUservice;

    @ResponseBody
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> login = iUservice.login(username, password);
        if (login.isSuccess()) {
            User data = login.getData();
            if (data.getRole() == Const.Role.ROLE_ADMIN) {
                //登录账号是管理员
                session.setAttribute(Const.CURRENT_USER, data);
            } else {
                return ServerResponse.createByErrorMessage("不是管理员你，无法登录");
            }
        }
        return login;
    }

}
