package org.pjj.thread.day04;

/**
 * ThreadLocal: 每个线程存储自身的数据, 更改不会影响其他线程
 *
 * @author PengJiaJun
 * @Date 2022/08/16 01:24
 */
public class Demo04ThreadLocal02 {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new MyRun()).start();
        }
    }

    public static class MyRun implements Runnable {
        @Override
        public void run() {
            Integer left = threadLocal.get();
            System.out.println(Thread.currentThread().getName() + " 得到了--> " + threadLocal.get());
            threadLocal.set(left - 1);
            System.out.println(Thread.currentThread().getName() + " 还剩下--> " + threadLocal.get());
        }
    }
}
