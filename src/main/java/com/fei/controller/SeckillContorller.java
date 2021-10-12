package com.fei.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fei.config.AccessLimit;
import com.fei.exception.GlobalException;
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
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 位置：com.fei.controller
 * 功能描述: 秒杀订单
 * 创建时间: 2021-10-06 22:43
 */
@SuppressWarnings("ALL")
@RequestMapping("/seckill")
@Controller
@Slf4j
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
    @Autowired
    private RedisScript<Long> script;

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
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public  ResBean doSeckill(@PathVariable String path, User user, Long goodsId){
        //第一步：判断用户是否为空
        if(user == null){
            return ResBean.error(ResBeanE.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //随机url检测
        boolean check = orderService.checkPath(user,goodsId,path);
        if(!check){
            return ResBean.error(ResBeanE.REQUEST_ILLEGAL);
        }
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
        //Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //用lua脚本实现redis分布式锁
        Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);

        if(stock<0){
            valueOperations.increment("seckillGoods:" + goodsId);
            return ResBean.error(ResBeanE.EMPTY_STOCK);
        }
        //下单
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        //发送下单消息
        //因为用了rabbitMq，变成了异步操作，能快速返回库存结果，会提供一个流量削峰的作用
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

    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @GetMapping("/path")
    @ResponseBody
    public ResBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request){
        if (user == null){
            return ResBean.error(ResBeanE.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //限制访问次数，5秒内访问5次
        String uri = request.getRequestURI();
        //captcha = "0";
//        Integer count = (Integer)valueOperations.get(uri + ":" + user.getId());
//        if(count == null){
//            valueOperations.set(uri + ":" + user.getId(), 1, 5, TimeUnit.SECONDS);
//        }else if (count < 5){
//            valueOperations.increment(uri + ":" + user.getId());
//        }else {
//            return ResBean.error(ResBeanE.ACCESS_LIMIT_REAHCED);
//        }
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if(!check)
            return ResBean.error(ResBeanE.ERROR_CAPTCHA);

        String str = orderService.createPath(user,goodsId);

        return ResBean.success(str);
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    @ResponseBody
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if(user == null || goodsId <0){
            throw new GlobalException(ResBeanE.REQUEST_ILLEGAL);
        }
        //设置请求头为输出的图片的类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码，将结果放入Redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId, captcha.text(), 60, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }

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
