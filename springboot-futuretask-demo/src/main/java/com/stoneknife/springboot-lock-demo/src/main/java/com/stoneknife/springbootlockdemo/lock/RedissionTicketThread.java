package com.stoneknife.springbootlockdemo.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;

public class RedissionTicketThread implements Runnable {

    private int ticketNum = 100;

    RLock lock = getRedissonLock();

    private RLock getRedissonLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        Redisson redisson = (Redisson) Redisson.create(config);
        RLock lock = redisson.getLock("ticketLock");
        return lock;
    }


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
