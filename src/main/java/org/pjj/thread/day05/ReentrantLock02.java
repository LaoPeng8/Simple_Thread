package org.pjj.thread.day05;

/**
 * 不可重入锁: 锁不可以延续使用
 *
 * @author PengJiaJun
 * @Date 2022/08/16 21:38
 */
public class ReentrantLock02 {

    NotReentrantLock lock = new NotReentrantLock();// new 一个 不可重入锁

    public void a() throws InterruptedException {
        lock.lock();//获取锁

        doSomething();// 干自己的事 (自己的业务)

        lock.unlock();//释放锁
    }

    public void doSomething() throws InterruptedException {
        lock.lock();//获取锁

        // .................... (获取到锁后, 处理业务)

        lock.unlock();//释放锁
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock02 test = new ReentrantLock02();
        test.a();//处理业务

        /**
         * 这里是不会打印的, 因为 在a()方法中 获得了 不可重入锁, 然后在a()方法中调用了 doSomething() 方法, 该方法也需要获取锁,
         * 但是 需要等待 a()方法释放锁了之后, doSomething()才能获取到锁, 但是 a()方法需要doSomething()执行完成之后, 才会释放锁
         * 于是 造成了 死锁的出现, 所以这里就执行不了了.
         *
         */
        System.out.println("业务处理完成, 返回前端数据, ok...");
    }
}

// 不可重入锁: 锁不可以延续使用
class NotReentrantLock {
    // 是否占用
    private boolean isLocked = false;

    //使用锁
    public synchronized void lock() throws InterruptedException {
        // 如果锁被使用, 则等待
        while(isLocked) {
            wait();
        }

        //如果锁没有被使用, 则转为使用状态
        isLocked = true;
    }

    //释放锁
    public synchronized void unlock() {
        isLocked = false;// 释放锁
        this.notify();
    }

}
