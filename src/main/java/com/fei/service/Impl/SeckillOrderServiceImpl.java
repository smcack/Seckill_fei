package com.fei.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fei.mapper.SeckillOrderMapper;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;
import com.fei.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * *
 *  服务实现类
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getId())
                .eq("goods_id", goodsId));
        if(null != seckillOrder){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:" + goodsId)){
            return -1L;
        }else {
            return 0L;
        }
    }
}
