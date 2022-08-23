package org.pjj.thread.day06;

/**
 * 测试 在同步方法中 调用 非同步方法, 那么这个非同步方法 是否 也是被加了锁的 (即 该非同步方法 是否只能有一个线程进入)
 *
 * 打印结果:
 * yesLock
 * 线程2noLock
 * noLock
 *
 * 从打印结果来看, 线程1 获取到锁 并打印 yesLock, 然后抱着锁等待, 此时线程2并不需要获取到锁 直接执行了 noLock, 然后线程1执行noLock然后释放锁
 *
 * 结论就是 在同步方法中调用非同步方法, 该非同步方法 是可以被 其他多个线程同时访问的.
 *
 * @author PengJiaJun
 * @Date 2022/08/23 17:35
 */
public class TestSynchronized {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            Test.yesLock();
        }).start();

        Thread.sleep(3000);//休眠 3 秒, 以方便 线程1先执行, 并获取到锁

        new Thread(() -> {
            System.out.print("线程2");
            Test.noLock();
        }).start();
    }
}

class Test {

    public static void yesLock() {
        synchronized (Test.class) {
            System.out.println("yesLock");

            try {
                Thread.sleep(10000);//休眠10秒, 以方便 线程2 调用 noLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            noLock();
        }
    }

    public static void noLock() {
        System.out.println("noLock");
    }

}
