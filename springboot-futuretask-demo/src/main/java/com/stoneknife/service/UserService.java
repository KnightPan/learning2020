package com.stoneknife.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserService {

    public Object getUserInfo() {
        Object user = getUser();
        Object account = getAccount();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("account", account);
        return map;
    }

    private Object getUser() {
        long beginTime = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis() - beginTime;
        System.out.println("getUser耗时" + time);
        return "1";
    }

    private Object getAccount() {
        long beginTime = System.currentTimeMillis();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis() - beginTime;
        System.out.println("getAccount耗时" + time);
        return "2";
    }
}
