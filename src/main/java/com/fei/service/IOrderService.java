package com.fei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.pojo.Order;
import com.fei.pojo.User;
import com.fei.vo.GoodsVo;

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

}
