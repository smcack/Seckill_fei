package com.fei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.pojo.User;
import com.fei.vo.LoginVo;
import com.fei.vo.ResBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fei
 * @since 2021-09-22
 */
public interface IUserService extends IService<User> {
    /**
     * 功能描述: 登录
     */
    ResBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);


    /**
     * 功能描述: 根据cookie获取用户
     */
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

}
