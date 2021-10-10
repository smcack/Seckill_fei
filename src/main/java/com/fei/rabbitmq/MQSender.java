package com.fei.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**消息发送者
 * @author ChenweiLin
 * @create 2021-08-08 15:53
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("queue","",msg);
//    }
//    public void send(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
//    }
//
//    public void send01(Object msg){
//        log.info("发送red消息"+msg);
//        //给消息绑定路由键queue.red
//        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
//    }
//
//    public void send02(Object msg){
//        log.info("发送green消息"+msg);
//        //给消息绑定路由键queue.green
//        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
//    }
//
//    public void send03(Object msg){
//        log.info("发送消息只被QUEUE01接收" + msg );
//        //发消息必须绑定具体的routingKey 通配符使用在交换机与队列绑定处
//        rabbitTemplate.convertAndSend("topicExchange","queue.red.message",msg);//可以被queue_topic01接收
//    }
//
//    public void send04(Object msg){
//        log.info("发送消息被两个queue接收" + msg );
//        //发消息必须绑定具体的routingKey 通配符使用在交换机与队列绑定处
//        rabbitTemplate.convertAndSend("topicExchange","message.queue.green.abc",msg);//可以被queue_topic01和queue_topic02接收
//    }
//
//    public void send05(String msg){
//        log.info("发送消息被两个queue接收" + msg);
//        //Header模式进行queue与消息的绑定
//        //通过发送头部信息
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("color","red");
//        messageProperties.setHeader("speed","fast");
//        Message message = new Message(msg.getBytes(),messageProperties);
//        rabbitTemplate.convertAndSend("headersExchange","",message);
//    }
//
//    public void send06(String msg){
//        log.info("发送消息被queue01_header接收" + msg );
//        //Header模式进行queue与消息的绑定
//        //通过发送头部信息
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("color","red");
//        messageProperties.setHeader("speed","xxxxx");
//        Message message = new Message(msg.getBytes(),messageProperties);
//        rabbitTemplate.convertAndSend("headersExchange","",message);
//    }


    /**
     * 发送秒杀信息
     * @param message
     */
    public void sendSeckillMessage(String message){
        log.info("发送消息"  + message);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }
}
