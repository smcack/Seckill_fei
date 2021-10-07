package com.fei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fei.pojo.Order;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;
import com.fei.service.IGoodsService;
import com.fei.service.IOrderService;
import com.fei.service.ISeckillOrderService;
import com.fei.vo.GoodsVo;
import com.fei.vo.ResBean;
import com.fei.vo.ResBeanE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 位置：com.fei.controller
 * 功能描述: 秒杀订单
 * 创建时间: 2021-10-06 22:43
 */
@RequestMapping("/seckill")
@Controller
public class SeckillContorller {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user,Long goodsId){
        //第一步：判断用户是否为空
        if(user == null){
            return "login";
        }
        //第二步：判断库存
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
        if(goods.getStockCount() < 1){
            model.addAttribute("errmsg", ResBeanE.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //第三步：判断是否重复抢购
        SeckillOrder seckillOrder =
                        seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
                        .eq("goods_id", goodsId));

        if(seckillOrder != null){
            model.addAttribute("errmsg", ResBeanE.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        //第四步：进入秒杀
        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods", goods);

        return "orderDetail";


    }
}
