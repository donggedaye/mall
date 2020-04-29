package com.mall.controller;

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

/**
 * @author muchewu
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUservice iUservice;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession httpSession) {
        //service ----> mybatis ---->dao
        ServerResponse<User> login = iUservice.login(username, password);
        if (login.isSuccess()) {
            httpSession.setAttribute(Const.CURRENT_USER, login.getData());
        }
        return login;
    }

    /**
     * 用户登出
     *
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpSession httpSession) {
        httpSession.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     */
    @ResponseBody
    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    public ServerResponse<String> register(User user) {
        return iUservice.register(user);
    }

    /**
     * 用户校验
     * @param str
     * @param type
     */
    @ResponseBody
    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    public ServerResponse<String> checkValid(String str,String type){
        return iUservice.checkValid(str,type);
    }
}
