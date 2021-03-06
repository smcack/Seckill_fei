package com.fei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;

/**
 * *
 *  服务类
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {


    Long getResult(User user, Long goodsId);
}
