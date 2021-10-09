package com.fei.config;//package com.lin.seckilllcw.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**Header模式
// * @author ChenweiLin
// * @create 2021-08-08 20:04
// */
//@Configuration
//public class RabbitMQHeaderCongfig {
//    private static final String QUEUE01 = "queue_header01";
//    private static final String QUEUE02 = "queue_header02";
//    private static final String EXCHANGE = "headerExchange";
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
//    public HeadersExchange headersExchange(){
//        return new HeadersExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding01(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("color", "red");
//        map.put("speed", "low");
//        //whereAny map中的头部信息只用满足任意一个
//        return BindingBuilder.bind(queue01()).to(headersExchange()).whereAny(map).match();
//    }
//
//    @Bean
//    public Binding binding02(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("color", "red");
//        map.put("fast", "low");
//        //whereAll map中的头部信息必须全部满足
//        return BindingBuilder.bind(queue02()).to(headersExchange()).whereAll(map).match();
//    }
//}
