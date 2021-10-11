package com.fei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.pojo.Order;
import com.fei.pojo.User;
import com.fei.vo.GoodsVo;
import com.fei.vo.OrderDetailVo;

/**
 * *
 *  服务类
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    boolean checkPath(User user, Long goodsId, String path);

    String createPath(User user, Long goodsId);

    boolean checkCaptcha(User user, Long goodsId, String captcha);

}
