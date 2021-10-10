package com.fei.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.fei.pojo.SeckillMessage;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;
import com.fei.service.IGoodsService;
import com.fei.service.IOrderService;
import com.fei.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author
 * @create 2021-08-08 15:56
 */
@Service
@Slf4j
public class MQReceiver {
    //监听“queue”队列
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("queue01接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("queue02接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void recevie03(Object msg){
//        log.info("queue_direct01接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void recevie04(Object msg){
//        log.info("queue_direct02接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void recevie05(Object msg){
//        log.info("queue_topic02接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void recevie06(Object msg){
//        log.info("queue_topic02接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void recevie07(Message msg){
//        log.info("QUEUE01接收Message对象" + msg);
//        log.info("queue_header01接收消息"+ new String(msg.getBody()) );
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void recevie08(Message msg){
//        log.info("QUEUE02接收Message对象" + msg);
//        log.info("queue_topic02接收消息"+ new String(msg.getBody()) );
//    }




    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收的消息；" + message);
        SeckillMessage seckillMessage = JSON.parseObject(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        //判断库存
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        if(goodsVo.getStockCount() < 1){
            return;
        }

        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return;
        }
        //下单操作
        orderService.seckill(user, goodsVo);
    }
}
