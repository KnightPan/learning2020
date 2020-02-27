package com.stoneknife.design.designpattern.templatemethod;

import org.springframework.stereotype.Component;

@Component
public class NormalCalculate implements ICalculate {

    @Override
    public double discount(double fee) {
        return fee;
    }

    @Override
    public String userType() {
        return "normal";
    }
}
