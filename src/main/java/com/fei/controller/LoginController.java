package com.fei.controller;

import com.fei.service.IUserService;
import com.fei.vo.LoginVo;
import com.fei.vo.ResBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: Seckill
 * @description: 登录
 * @author: fei
 * @create: 2021-09-23 11:06
 */

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {


    @Autowired
    private IUserService userService;

    /**
     * 登录跳转
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }



    @RequestMapping("/doLogin")
    @ResponseBody
    public ResBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        return userService.doLogin(loginVo,request,response);
    }

//15e13709a2962472e325957d793b9ed79ab5b56f


}
