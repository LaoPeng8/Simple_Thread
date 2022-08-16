package org.pjj.thread.day05;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁: java.util.concurrent.locks.ReentrantLock (不用我们自己写, Java以及是实现了一个可重入锁)
 *
 * @author PengJiaJun
 * @Date 2022/08/16 22:17
 */
public class ReentrantLock04 {

    ReentrantLock lock = new ReentrantLock();//Java实现的可重入锁

    public void a() throws InterruptedException {
        lock.lock();//获取锁

        System.out.println(lock.getHoldCount());// 1

        doSomething();// 干自己的事 (自己的业务)

        lock.unlock();//释放锁
    }

    public void doSomething() throws InterruptedException {
        lock.lock();//获取锁

        System.out.println(lock.getHoldCount());// 2

        // .................... (获取到锁后, 处理业务)

        lock.unlock();//释放锁
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock03 test = new ReentrantLock03();
        test.a();//处理业务

        /**
         * 会成功打印
         * 在 a()方法中获取锁后, a()方法调用 doSomething()方法, 由于同属于main线程, 使用a()方法获取到的锁, 会被带入 doSomething()方法,
         * 所以 doSomething()方法就直接获取到锁, 并执行业务, 直到最终返回
         */
        System.out.println("业务处理完成, 返回前端数据, ok...");
    }
}
