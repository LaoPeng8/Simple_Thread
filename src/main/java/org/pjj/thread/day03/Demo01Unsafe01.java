package org.pjj.thread.day03;

/**
 * 测试 线程不安全的情况
 *
 * 线程不安全, 出现了 同一张被买2次的情况 以及 出现 负数的票
 *
 * @author PengJiaJun
 * @Date 2022/08/13 17:42
 */
public class Demo01Unsafe01 {
    public static void main(String[] args) {
        UnsafeWeb12306 unsafeWeb12306 = new UnsafeWeb12306();

        new Thread(unsafeWeb12306, "购票人:小明").start();
        new Thread(unsafeWeb12306, "购票人:小红").start();
        new Thread(unsafeWeb12306, "购票人:小黑").start();

    }
}

class UnsafeWeb12306 implements Runnable {
    private int ticket = 10;//票数
    private boolean flag = true;//控制线程停止的标志位


    @Override
    public void run() {
        while(flag) {
            test();
        }
    }

    /**
     * 线程不安全, 出现了 同一张被买2次的情况 以及 出现 负数的票
     *
     * 负数的票出现的情况 (假设 还有1张票)
     *      * A线程进来 发现 ticket !<= 0(还有一张票), 然后A线程进行延时200毫秒, 结束后, A线程回到就绪状态 等待CPU调度,
     *      * 此时CPU调度B线程, B线程进来发现 ticket !<= 0(还有一张票, A线程还没有执行ticket--), B线程sleep 200毫秒,
     *      * 完事后, 不管CPU先调度 A线程还是B线程, 他们都会执行 打印票数 同时--, 那么就会出现 只有1张票, A也打印了并--, B也打印了并--, 就出现了负数
     *
     * 同一张被买2次的情况 (假设 ticket=10)
     *      * 线程是操作自己的工作空间, 不能操作主内存, ticket是存储在主内存的.
     *      * 线程A使用ticket是从 主内存拷贝一份 到自己的工作空间操作
     *      * 线程B使用ticket也是从 主内存拷贝一份 到自己的工作空间操作
     *      * 此时 线程A将 ticket--了(9), 但是值还在自己的工作空间, 还没有将值同步到主内存 (此时主内存ticket=10, A线程ticket=9)
     *      * 线程B读取了 主内存中的ticket到自己的工作空间使用, 然后打印然后--, 然后同步到主内存 (此时主内存ticket=10, B线程ticket=9)
     *      * 同步到主内存后, 主内存ticket=9, 但是 9 已经被打印两次了...
     *
     */
    public void test() {
        if(ticket <= 0) {
            flag = false;
            return;
        }

        try {
            //模拟延时
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " ---> " + ticket--);
    }
}
