package org.pjj.thread.day04;

/**
 * InheritableThreadLocal: 继承上下文环境的数据 (InheritableThreadLocal 是 继承上下文环境的 ThreadLocal)
 * InheritableThreadLocal: 继承上下文环境的数据, 将父线程的上下文环境 拷贝一份 给子线程
 *
 * @author PengJiaJun
 * @Date 2022/08/16 01:40
 */
public class Demo04ThreadLocal04 {
    private static ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());// null
        threadLocal.set(2);

        new Thread(() -> {
            //结果为 2, 因为 InheritableThreadLocal 会继承上下文环境, 本线程由main线程开辟, 所以显然会继承 main线程的上下文环境 所以为 2
            System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());// 2
        }).start();
    }

}
