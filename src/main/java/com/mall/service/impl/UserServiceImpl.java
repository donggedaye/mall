package com.mall.service.impl;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUservice;
import com.mall.utill.MD5Util;
import org.aoju.bus.core.utils.ObjectUtils;
import org.aoju.bus.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author muchewu
 */
@Service("IUserService")
public class UserServiceImpl implements IUservice {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (StringUtils.isEmpty(resultCount)) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        //todo 密码登录MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, password);
        if (ObjectUtils.isEmpty(user)) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse stringServerResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!stringServerResponse.isSuccess()) {
            return stringServerResponse;
        }
        stringServerResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!stringServerResponse.isSuccess()) {
            return stringServerResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int i = userMapper.checkUsername(str);
                if (i > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int i = userMapper.checkEmail(str);
                if (i > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            } else {
                return ServerResponse.createByErrorMessage("参数错误");
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> stringServerResponse = this.checkValid(username, Const.USERNAME);
        if (stringServerResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String questionUsername = userMapper.selectQuestionUsername(username);
        if (StringUtils.isNotBlank(questionUsername)) {
            return ServerResponse.createBySuccess(questionUsername);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int i = userMapper.checkAnswer(username, question, answer);
        if (i > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse<String> stringServerResponse = this.checkValid(username, Const.CURRENT_USER);
        if (stringServerResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String key = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(key)) {
            return ServerResponse.createByErrorMessage("token过期或失效");
        }

        if (StringUtils.equals(forgetToken, key)) {
            String md5EncodeUtf8 = MD5Util.MD5EncodeUtf8(passwordNew);
            int i = userMapper.updatePasswordByUsername(username, passwordNew);
            if (i > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            } else {
                return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
            }
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

}
