package com.stoneknife.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * Thread线程只能接收Runnable类型的参数，
 * Task如果需要交给Thread多线程处理必须实现Runnable接口
 * @param <T>
 */
public class MyFutureTask<T> implements Runnable {

    MyCallable<T> callable;

    T result = null;

    String state = "NEW";

    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public MyFutureTask(MyCallable<T> callable) {
        this.callable = callable;
    }

    public T get() {

        //只有当线程执行完毕才返回数据
        if("FINISH".equals(state)) {
            return result;
        }

        //否则将调用get()方法的当前线程放入List中
        waiters.add(Thread.currentThread());
        while(!"FINISH".equals(state)){
            //当前线程中止
            LockSupport.park();
        }

        return result;
    }

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            state = "FINISH";
        }

        //线程执行完以后，唤醒所有被中止的线程
        Thread waiter = waiters.poll();
        while(waiter != null) {
            LockSupport.unpark(waiter);
            waiter = waiters.poll();
        }
    }
}
