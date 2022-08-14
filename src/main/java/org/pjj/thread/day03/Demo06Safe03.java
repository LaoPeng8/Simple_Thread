package org.pjj.thread.day03;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 集合线程不安全, 加入 synchronized 使其安全
 *
 * @author PengJiaJun
 * @Date 2022/08/14 16:52
 */
public class Demo06Safe03 {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                synchronized (list) {//加入同步块, 保证同一时刻, 只能有一个线程操作 list, 保证了线程安全
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }

        /**
         * 没有使用 synchronized 同步块时
         * 结果就是 ArrayList 偶尔10000, 偶尔9997 (线程不安全)
         *
         * 使用 synchronized 同步块时
         * 结果就是 ArrayList 一直10000
         */
        Thread.sleep(2000);
        System.out.println(list.size());
    }
}
