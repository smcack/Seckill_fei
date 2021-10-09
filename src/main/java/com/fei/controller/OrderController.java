package com.fei.controller;


import com.fei.pojo.User;
import com.fei.service.IOrderService;
import com.fei.vo.OrderDetailVo;
import com.fei.vo.ResBean;
import com.fei.vo.ResBeanE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * *
 *  前端控制器
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private IOrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public ResBean detail(User user, Long orderId) {
        if (user == null) {
            return ResBean.error(ResBeanE.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return ResBean.success(detail);
    }

}
