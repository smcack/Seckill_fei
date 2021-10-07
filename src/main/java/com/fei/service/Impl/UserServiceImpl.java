package com.fei.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fei.exception.GlobalException;
import com.fei.mapper.UserMapper;
import com.fei.pojo.User;
import com.fei.service.IUserService;
import com.fei.utils.CookieUtil;
import com.fei.utils.MD5Utils;
import com.fei.utils.UUIDUtil;
import com.fei.utils.ValidatorUtil;
import com.fei.vo.LoginVo;
import com.fei.vo.ResBean;
import com.fei.vo.ResBeanE;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fei
 * @since 2021-09-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**功能描述：登录
     *
     */
    @Override
    public ResBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) ){
//            return ResBean.error(ResBeanE.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return  ResBean.error(ResBeanE.MOBILE_ERROR);
//        }
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if(user == null){
            throw new GlobalException(ResBeanE.LOGIN_ERROR);
        }
        //判断密码是否正确
        if(!MD5Utils.formPassToDBPass(password,user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(ResBeanE.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        //讲用户信息存入到redis当中
        redisTemplate.opsForValue().set("user:"+ticket, user);
        //request.getSession().setAttribute(ticket,user);  //存入redis之后就不需要用session了
        CookieUtil.setCookie(request, response, "userTicket",ticket);



        return ResBean.success(ticket);
    }



    /**
     * 功能描述: 根据cookie获取用户
     */
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        System.out.println("user" + user.toString());
        return user;
    }

//
    /**
     * 功能描述:更新密码
     * 这个点的核心是
     * 当我们对数据库进行更改操作的时候，crud的rud时，一定要对redis之前所关联的数据进行删除
     */
//    @Override
//    public ResBean updatePassword(String userTicket, String password, HttpServletRequest request,
//                                   HttpServletResponse response) {
//        User user = getUserByCookie(userTicket, request, response);
//        if (user == null) {
//            throw new GlobalException(ResBeanE.MOBILE_NOT_EXIST);
//        }
//        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSlat()));
//        int result = userMapper.updateById(user);
//        //更新了密码之后要删除redis之前的数据
//        if (1 == result) {
//            //删除Redis
//            redisTemplate.delete("user:" + userTicket);
//            return ResBean.success();
//        }
//        return ResBean.error(ResBeanE.PASSWORD_UPDATE_FAIL);
//    }
}
