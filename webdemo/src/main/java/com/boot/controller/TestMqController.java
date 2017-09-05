package com.boot.controller;

import com.boot.config.AmqpConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mq")
public class TestMqController {

    private final static String MyRouteKey = "spring-boot-routingKey-test";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RequestMapping("/send")
    @ResponseBody
    public Object send(){
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE,AmqpConfig.ROUTINGKEY,"send one");
        return "success";
    }
}
