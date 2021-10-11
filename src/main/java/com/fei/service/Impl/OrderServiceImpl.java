package com.fei.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fei.exception.GlobalException;
import com.fei.pojo.Order;
import com.fei.mapper.OrderMapper;
import com.fei.pojo.SeckillGoods;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;
import com.fei.service.IGoodsService;
import com.fei.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fei.service.ISeckillGoodsService;
import com.fei.service.ISeckillOrderService;
import com.fei.utils.MD5Utils;
import com.fei.utils.UUIDUtil;
import com.fei.vo.GoodsVo;
import com.fei.vo.OrderDetailVo;
import com.fei.vo.ResBeanE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * *
 *  服务实现类
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 秒杀
     *
     * @param user
     * @param goods
     * @return
     */

    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品表减库存  超卖问题定位于此
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        //判断库存//超卖问题 更新库存之前 判断库存是否为零
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goods.getId()).gt("stock_count", 0));
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存,没有库存，标记到redis
            valueOperations.set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        //把秒杀订单信息存入redis
        valueOperations.set("order:" + user.getId() + ":" + goods.getId(), seckillOrder,10, TimeUnit.SECONDS);
        return order;
    }


    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(ResBeanE.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoById(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;
    }


    /**
     * 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Utils.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        return str;
    }


    /**
     * 校验秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path))
            return false;
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 校验验证码
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0)
            return false;
        String checkCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(checkCaptcha);
    }
}

