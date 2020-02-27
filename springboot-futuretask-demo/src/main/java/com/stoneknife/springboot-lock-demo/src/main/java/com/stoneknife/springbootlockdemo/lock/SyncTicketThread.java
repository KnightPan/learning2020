package com.stoneknife.springbootlockdemo.lock;

public class SyncTicketThread implements Runnable {

    private int ticketNum = 100;

    @Override
    public void run() {
        while (ticketNum > 0) {
            synchronized (this){
                if(ticketNum > 0) {
                    try {
                        //模拟买票需要花费的时间
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "售出第" + ticketNum-- + "张票");
                }
            }
        }
    }
}
