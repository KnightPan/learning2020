package com.stoneknife.service;

import com.stoneknife.task.MyCallable;
import com.stoneknife.task.MyFutureTask;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Component
public class UserService_MyFutureTask {

    public Object getUserInfo() {
        MyFutureTask<Object> userTask = new MyFutureTask<>(new MyCallable<Object>() {
            @Override
            public Object call() throws Exception {
                Object user = getUser();
                return user;
            }
        });
        MyFutureTask<Object> accountTask = new MyFutureTask<>(new MyCallable<Object>() {
            @Override
            public Object call() throws Exception {
                Object account = getAccount();
                return account;
            }
        });
        new Thread(userTask, "user-thread").start();
        new Thread(accountTask, "account-thread").start();

        Map<String, Object> map = new HashMap<>();
        map.put("user", userTask.get());
        map.put("account", accountTask.get());
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
