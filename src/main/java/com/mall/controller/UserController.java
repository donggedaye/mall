package com.mall.controller;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUservice;
import org.aoju.bus.core.utils.ObjectUtils;
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
     *
     * @param user
     */
    @ResponseBody
    @RequestMapping(value = "register.do", method = RequestMethod.GET)
    public ServerResponse<String> register(User user) {
        return iUservice.register(user);
    }

    /**
     * 用户校验
     *
     * @param str
     * @param type
     */
    @ResponseBody
    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    public ServerResponse<String> checkValid(String str, String type) {
        return iUservice.checkValid(str, type);
    }

    /**
     * 获取用户token
     *
     * @param session
     */
    @ResponseBody
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User attribute = (User) session.getAttribute(Const.CURRENT_USER);
        if (ObjectUtils.isNotEmpty(attribute)) {
            return ServerResponse.createBySuccess(attribute);
        }
        return ServerResponse.createByErrorMessage("用户未登录，获取不到用户的信息");
    }

    /**
     * 获取用户找回密码的问题
     *
     * @param username
     */
    @ResponseBody
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUservice.selectQuestion(username);
    }

    /**
     * 忘记密码回答问题的答案
     *
     * @param username
     * @param question
     * @param answer
     */
    @ResponseBody
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUservice.checkAnswer(username, question, answer);
    }

    /**
     * 密码重置
     */
    @ResponseBody
    @RequestMapping(value ="forget_reset_password.do", method = RequestMethod.GET)
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        return iUservice.forgetRestPassword(username, passwordNew, forgetToken);
    }
}
