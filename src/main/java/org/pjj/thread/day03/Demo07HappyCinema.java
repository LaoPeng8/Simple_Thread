package org.pjj.thread.day03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 快乐影院
 * 加深对于 synchronized 的理解
 *
 * @author PengJiaJun
 * @Date 2022/08/14 21:34
 */
public class Demo07HappyCinema {
    public static void main(String[] args) {
        Cinema cinema = new Cinema(new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10)), "《激冻树影院》");
        new Thread(new Customer(cinema, Arrays.asList(9, 10)), "me and my girl").start();
        new Thread(new Customer(cinema, Arrays.asList(66)), "刘康桑").start();

    }
}

//顾客
class Customer implements Runnable {
    Cinema cinema;//影院, 顾客去哪个影院看电影
    List<Integer> seats;//需要几个位置

    public Customer(Cinema cinema, List<Integer> seats) {
        this.cinema = cinema;
        this.seats = seats;
    }

    @Override
    public void run() {
        boolean flag;
        synchronized (cinema) {
            flag = cinema.bookTickets(seats);
        }
        if(flag) {
            System.out.println(Thread.currentThread().getName() + " >>> 购票成功 座位号: " + seats);
        }else {
            System.out.println(Thread.currentThread().getName() + " >>> 购票失败, 位置不够");
        }
    }
}

//影院
class Cinema {
    List<Integer> available;//可用的位置
    String name;//名称

    public Cinema(List<Integer> available, String name) {
        this.available = available;
        this.name = name;
    }

    public boolean bookTickets(List<Integer> seats) {
        System.out.println("欢迎光临 " + this.name + " 当前可用位置: " + available);

        //判断 需要购买的座位 是否全部在 可用的座位中
        if(available.containsAll(seats)) {
            //如果 需要购买的座位, 在可用的座位中都有, 则进行购买, 并返回 购买成功
            available.removeAll(seats);//从可用座位中 删除 本次购买的座位
            return true;
        }else {
            //如果 需要购买的座位 有一个不在 可用座位中, 则直接返回 购买失败
            return false;
        }

    }
}
