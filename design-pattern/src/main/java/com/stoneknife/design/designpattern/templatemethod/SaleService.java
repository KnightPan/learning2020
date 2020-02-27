package com.stoneknife.design.designpattern.templatemethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SaleService {

    ConcurrentHashMap<String, ICalculate> calculateMap = new ConcurrentHashMap<>();

    //spring自动注入接口实现类
    @Autowired
    public SaleService(List<ICalculate> calculateList){
        for(ICalculate calculate : calculateList){
            calculateMap.putIfAbsent(calculate.userType(), calculate);
        }
    }

    public double sale(String userType, double fee){
        ICalculate calculate = calculateMap.get(userType);
        if(calculate != null){
            return calculate.discount(fee);
        }
        return fee;
    }
}
