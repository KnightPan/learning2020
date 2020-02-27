package com.stoneknife.design.designpattern.observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    ApplicationContext applicationContext;

    public void saveOrder(){

        //保存订单
        System.out.println("1、订单保存成功");

        OrderEvent orderEvent = new OrderEvent(applicationContext);
        applicationContext.publishEvent(orderEvent);
        //发短信
        //System.out.println("2、短信发送成功");
    }
}
