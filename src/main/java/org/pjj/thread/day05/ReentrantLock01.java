package org.pjj.thread.day05;

/**
 *
 * 锁作为并发共享数据保证一致性的工具, 大多数内置锁都是可重入的, 也就是说, 如果某个线程试图获取一个已经由它自己持有的锁时,
 * 那么这个请求会立刻成功, 并且会将这个锁的计数值+1, 而当线程退出同步代码块时, 计数器将会递减, 当计数值为 0时, 锁释放。
 *
 * 如果没有可重入锁的支持, 在第二次企图获得锁时将会进入死锁状态
 *
 * 可重入锁: 锁可以延续使用
 *
 * @author PengJiaJun
 * @Date 2022/08/16 21:19
 */
public class ReentrantLock01 {

    public void test() {
        //第一次获取锁
        synchronized (this) {
            while(true) {
                /**
                 * 第二次获取锁
                 * 如果 不是可重入
                 * 则, 第一次获取锁之后, 这里第二次获取锁 会进入 阻塞等待, 等待这把锁被释放 然后这里获取锁进入执行,
                 * 但是, 外层的syn也是在等着这里执行完之后, 才会释放锁, 这就造成了死锁
                 *
                 * 实际测试 这里是会打印的, 说明 synchronized 是可重入的锁
                 * 即 外层syn获取到了这把锁, 里层的syn(或者里层的其他方法)则也是可以获取到这把锁的, 这就是可重入锁
                 * 即 外层的syn会把锁 往 里层带
                 */
                synchronized (this) {
                    System.out.println("ReentrantLock");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static void main(String[] args) {
        new ReentrantLock01().test();

    }

}
