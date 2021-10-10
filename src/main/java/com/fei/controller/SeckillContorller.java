package com.fei.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fei.pojo.Order;
import com.fei.pojo.SeckillMessage;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;
import com.fei.rabbitmq.MQSender;
import com.fei.service.IGoodsService;
import com.fei.service.IOrderService;
import com.fei.service.ISeckillOrderService;
import com.fei.vo.GoodsVo;
import com.fei.vo.ResBean;
import com.fei.vo.ResBeanE;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 位置：com.fei.controller
 * 功能描述: 秒杀订单
 * 创建时间: 2021-10-06 22:43
 */
@SuppressWarnings("ALL")
@RequestMapping("/seckill")
@Controller
public class SeckillContorller implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;

    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();


    /**
     * 秒杀功能
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, User user,Long goodsId){
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


    /**
     * 秒杀功能页面静态化
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public  ResBean doSeckill(User user,Long goodsId){
        //第一步：判断用户是否为空
        if(user == null){
            return ResBean.error(ResBeanE.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        SeckillOrder seckillOrder =
                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
                        .eq("goods_id", goodsId));
        if(seckillOrder != null){
            return ResBean.error(ResBeanE.REPEATE_ERROR);
        }
       // 内存标记，减少redis的访问
//        if(EmptyStockMap.get(goodsId)){
//            return  ResBean.error(ResBeanE.EMPTY_STOCK);
//        }
        //预见库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if(stock<0){
            valueOperations.increment("seckillGoods:" + goodsId);
            return ResBean.error(ResBeanE.EMPTY_STOCK);
        }
        //下单
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        //发送下单消息
        mqSender.sendSeckillMessage(JSON.toJSONString(seckillMessage));
        return ResBean.success(0);
    }
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public ResBean getResult(User user,Long goodsId){
        if(user == null){
            return ResBean.error(ResBeanE.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return ResBean.success(orderId);
    }

//        //第二步：判断库存
//        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
//        if(goods.getStockCount() < 1){
//            return ResBean.error(ResBeanE.EMPTY_STOCK);
//        }
//        //第三步：判断是否重复抢购
//        SeckillOrder seckillOrder =
//                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
//                        .eq("goods_id", goodsId));
//        if(seckillOrder != null){
//            return ResBean.error(ResBeanE.REPEATE_ERROR);
//        }
        //第四步：进入秒杀
        //Order order = orderService.seckill(user,goods);
//        return ResBean.success(order);
//    }

    /**
     * 系统初始化，把商品库存数量加载到Redis
     * @throws Exception
     */

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list =goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //redis提前获取库存
        list.forEach(goodsVo  ->{
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
        });
        System.out.println("系统初始化，商品库存数量加载到Redis");
    }
}
