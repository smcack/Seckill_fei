package com.fei.controller;


import com.fei.rabbitmq.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fei
 * @since 2021-09-22
 */
@Controller
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private MQSender mqSender;
//
//
//    /**
//     * 测试发送
//     */
//    @RequestMapping("mg")
//    @ResponseBody
//    public void mq(){
//        mqSender.send03("hello,red");
//    }
//    @RequestMapping("mg01")
//    @ResponseBody
//    public void mq01(){
//        mqSender.send04("hello,green");
//    }
//

}
