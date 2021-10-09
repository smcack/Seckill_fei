package com.fei.config;//package com.lin.seckilllcw.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**Rabbitmq配置类 direct交换机与队列示例
// * @author ChenweiLin
// * @create 2021-08-08 16:44
// */
//@Configuration
//public class RabbitMQDirectConfig {
//    private static final String QUEUE01 = "queue_direct01";
//    private static final String QUEUE02 = "queue_direct02";
//    private static final String EXCHANGE = "directExchange";
//    private static final String ROUTINGKEY01 = "queue.red";//路由键01
//    private static final String ROUTINGKEY02 = "queue.green";//路由键02
//
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
//    public DirectExchange directExchange(){
//        return new DirectExchange(EXCHANGE);
//    }
//
//    //queue01与directExchange绑定并加上路由键   （队列与交换机绑定）
//    @Bean
//    public Binding binding01(){
//        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
//    }
//
//    //queue02与directExchange绑定并加上路由键   （队列与交换机绑定）
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
//    }
//}
