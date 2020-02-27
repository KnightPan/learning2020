package com.stoneknife.synchronize;

import java.util.concurrent.TimeUnit;

public class SynchronizeDemo {

    private static boolean ls = true;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(SynchronizeDemo.ls) {
                    //进入同步代码块之前，
                    // 线程先把工作内存的共享变量清空，
                    // 然后重新加载主内存的共享变量
                    synchronized (this){
                        i++;
                    }
                }
                System.out.println(i);
            }
        }).start();


        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("我被主线程修改了");
            SynchronizeDemo.ls = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
