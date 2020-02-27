package com.stoneknife.springbootlockdemo.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTicketThread implements Runnable {

    private int ticketNum = 100;

    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (ticketNum > 0) {
            lock.lock();
            try {
                if(ticketNum > 0) {
                    try {
                        //模拟买票需要花费的时间
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "售出第" + ticketNum-- + "张票");
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
