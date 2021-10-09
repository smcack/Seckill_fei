//package com.fei.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
///**Rabbitmq配置类 fanout交换机与队列示例
// * @author ChenweiLin
// * @create 2021-08-08 15:47
// */
//@Configuration
//public class RabbitMQFanoutConfig{
//    private static final String QUEUE01 = "queue_fanout01";
//    private static final String QUEUE02 = "queue_fanout02";
//    private static final String EXCHANGE = "fanoutExchange";
//
//    //准备队列
//    //Queue是import org.springframework.amqp.core.Queue包下的
//    @Bean
//    public Queue queue(){
//        return new Queue("queue",true);//第二个参数为持久化
//    }
//
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(EXCHANGE);
//    }
//
//    //queue01与fanoutExchange绑定   （队列与交换机绑定）
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    //queue02与fanoutExchange绑定   （队列与交换机绑定）
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
//}
