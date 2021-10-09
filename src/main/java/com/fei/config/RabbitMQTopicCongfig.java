//package com.fei.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**Header模式
// * @author ChenweiLin
// * @create 2021-08-08 20:04
// */
//@Configuration
//public class RabbitMQTopicCongfig {
//    private static final String QUEUE = "seckillQueue";
//    private static final String EXCHANGE = "seckillExchange";
//
//    @Bean
//    public Queue queue(){
//        return new Queue(QUEUE);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return  new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding(){
//        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
//    }
//}
