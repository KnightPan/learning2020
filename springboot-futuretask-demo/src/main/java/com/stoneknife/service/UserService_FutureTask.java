package com.stoneknife.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Component
public class UserService_FutureTask {

    public Object getUserInfo() {
        FutureTask<Object> userTask = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object user = getUser();
                return user;
            }
        });
        FutureTask<Object> accountTask = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object account = getAccount();
                return account;
            }
        });
        new Thread(userTask).start();
        new Thread(accountTask).start();

        Map<String, Object> map = new HashMap<>();
        try {
            map.put("user", userTask.get());
            map.put("account", accountTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
