package com.stoneknife.design.designpattern.observer;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class LoadProductType implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("Spring启动之后，执行一些初始化的操作（加载数据库的数据、初始化）");
    }
}
