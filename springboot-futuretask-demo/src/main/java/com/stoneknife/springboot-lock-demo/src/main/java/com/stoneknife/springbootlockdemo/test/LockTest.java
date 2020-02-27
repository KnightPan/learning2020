package com.stoneknife.springbootlockdemo.test;

import com.stoneknife.springbootlockdemo.lock.LockTicketThread;
import com.stoneknife.springbootlockdemo.lock.RedissionTicketThread;
import com.stoneknife.springbootlockdemo.lock.SyncTicketThread;
import com.stoneknife.springbootlockdemo.lock.TicketThread;

public class LockTest {
    public static void main(String[] args) {
        //TicketThread tt = new TicketThread();
        //SyncTicketThread tt = new SyncTicketThread();
        //LockTicketThread tt = new LockTicketThread();
        RedissionTicketThread tt = new RedissionTicketThread();

        for (int i=1; i<=3; i++) {
            Thread t = new Thread(tt, i + "号窗口");
            t.start();
        }
    }
}
