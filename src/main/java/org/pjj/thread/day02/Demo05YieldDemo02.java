package org.pjj.thread.day02;

/**
 * yield 礼让线程, 让当前正在执行线程暂停, 不是阻塞线程, 而是将线程从 运行状态 转入 就绪状态, 让CPU调度器重新调度
 *
 * yield 礼让线程, 避免一个线程 占用系统资源过久
 *
 * @author PengJiaJun
 * @Date 2022/08/12 23:34
 */
public class Demo05YieldDemo02 {
    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("lambda... " + i);
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                if(i % 20 == 0) {
                    Thread.yield();//礼让线程, 将线程从 运行状态 转入 就绪状态, 让CPU调度器重新调度
                }
                System.out.println("main... " + i);
            }
        }).start();
    }
}
