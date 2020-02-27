package com.stoneknife.design.designpattern.observer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class OrderEvent extends ApplicationContextEvent {
    public OrderEvent(ApplicationContext source) {
        super(source);
    }
}
