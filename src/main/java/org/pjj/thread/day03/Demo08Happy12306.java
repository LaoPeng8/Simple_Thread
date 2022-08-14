package org.pjj.thread.day03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 快乐 火车票网
 *
 * 加深对 synchronized 的理解
 *
 * @author PengJiaJun
 * @Date 2022/08/14 23:47
 */
public class Demo08Happy12306 {
    public static void main(String[] args) {
        Web12306 web12306 = new Web12306(2, "《WEB12306》");

        new Passenger(web12306, "刘康桑", 2).start();
        new Passenger(web12306, "王磊桑", 1).start();

    }
}

//顾客
class Passenger extends Thread {
    int seats;//需购买的票数

    public Passenger(Runnable target, String name, int seats) {
        super(target, name);
        this.seats = seats;
    }
}

//影院
class Web12306 implements Runnable {
    int available;//可用的位置
    String name;//名称

    public Web12306(int available, String name) {
        this.available = available;
        this.name = name;
    }

    @Override
    public void run() {
        Passenger passenger = (Passenger) Thread.currentThread();
        boolean flag = this.bookTickets(passenger.seats);
        if(flag) {
            System.out.println(Thread.currentThread().getName() + " >>> 购票成功");
        }else {
            System.out.println(Thread.currentThread().getName() + " >>> 购票失败, 余票不够");
        }
    }

    public synchronized boolean bookTickets(int seats) {
        System.out.println("欢迎光临 " + this.name + " 当前余票: " + available);
        if(available - seats < 0) {
//            throw new RuntimeException("您需购买" + seats +"张票, 余票为"+available + ", 余票不足 购买失败");
            return false;
        }
        available = available - seats;
        return true;
    }
}
