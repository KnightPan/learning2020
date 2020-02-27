package com.stoneknife.design.designpattern.templatemethod;

import org.springframework.stereotype.Component;

@Component
public class VipCalculate implements ICalculate {

    @Override
    public double discount(double fee) {
        return fee * 0.8;
    }

    @Override
    public String userType() {
        return "vip";
    }
}
