package org.pjj.thread.day05;

/**
 * 可重入锁: 锁可以延续使用 + 计数器
 *
 * @author PengJiaJun
 * @Date 2022/08/16 21:52
 */
public class ReentrantLock03 {

    ReLock lock = new ReLock();// new 一个 可重入锁

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

// 可重入锁: 锁可以延续使用
class ReLock {
    // 是否占用
    private boolean isLocked = false;
    private Thread lockedBy = null; //存储, 获取本锁的线程
    private int holdCount = 0;

    //使用锁
    public synchronized void lock() throws InterruptedException {
        Thread t = Thread.currentThread();//获取到当前线程
        // 如果锁被使用 同时 这次获取锁的线程 不是 持有本锁的线程 则等待 (换言之 如果本次请求锁的线程 本身就是持有本锁的线程, 则不会等待, 相当于直接获取到锁)
        while(isLocked && lockedBy != t) {
            wait();
        }

        //如果锁没有被使用, 则转为使用状态, 同时存储 持有本锁的线程
        isLocked = true;
        lockedBy = t;
        holdCount++;//同时 计数器 + 1
    }

    //释放锁
    public synchronized void unlock() {
        // 释放锁 首先释放锁的线程需要 == 持有本锁的线程, (否则你释放个勾八)
        // 然后就是 计数器 - 1, 只有计数器为 0的时候, 才会真正释放锁. (计数器不为0的时候, 说明是被重入了)
        if(Thread.currentThread() == lockedBy) {
            holdCount--;
            if(holdCount == 0) {
                isLocked = false;// 释放锁
                this.notify();
                lockedBy = null;
            }
        }
    }

    public int getHoldCount() {
        return holdCount;
    }
}
