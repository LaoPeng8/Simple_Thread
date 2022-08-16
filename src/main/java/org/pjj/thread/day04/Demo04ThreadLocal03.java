package org.pjj.thread.day04;

/**
 * ThreadLocal: 分析上下文环境
 * 1.构造器中的threadLocal, 是哪个线程调用则属于哪个线程
 * 2.run()方法中的threadLocal, 本线程自己的
 *
 * @author PengJiaJun
 * @Date 2022/08/16 01:30
 */
public class Demo04ThreadLocal03 {
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

    public static void main(String[] args) {
        new Thread(new MyRun()).start();
    }

    public static class MyRun implements Runnable {
        public MyRun() {
            // MyRun这个类中的 构造器 中的 threadLocal 居然是属于 main 线程的
            // 原因是 因为在main线程中 new MyRun() 所以, 执行到本构造器时 还是属于main线程, 所以threadLocal是属于main线程的
            System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());//main --> 1

            threadLocal.set(8888);//这里修改的threadLocal是不会影响到下面 run方法中的ThreadLocal的值的, 因为这里的threadLocal属于两个不同的线程
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 还剩下--> " + threadLocal.get());//Thread-0 还剩下--> 1
        }
    }
}
