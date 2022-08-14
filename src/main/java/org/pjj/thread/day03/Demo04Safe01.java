package org.pjj.thread.day03;

/**
 * 线程同步
 * 由于多个线程共享同一块存储空间, 在带来方便的同时, 也带来了访问冲突的问题。
 * 为了保证数字在方法中被访问时的在正确性, 在访问时加入锁机制(synchronized), 当一个线程获取对象的排他锁, 独占资源, 其他线程必须等待, 使用后释放锁。
 * 锁(synchronized)会导致以下问题
 *      * 一个线程持有锁会导致其他所有需要此锁的线程挂起.   (我的理解  并行 变为 串行)
 *      * 在多线程竞争下, 加锁, 释放锁会导致比较多的上下文切换和调度延时, 引起性能问题
 *      * 如果一个优先级高的线程等待一个优先级低的线程释放锁会导致优先级倒置, 引起性能问题
 *
 * synchronized包含两种用法, synchronized方法 和 synchronized块
 * synchronized方法控制对 "成员变量|类变量" 对象的访问 每个对象对应一把锁, 每个synchronized方法都必须获得调用该方法的对象的锁方能执行,
 * 否则所属线程阻塞, 方法一旦执行, 就独占该锁, 直到从该方法返回时才将锁释放, 此后被阻塞的线程方能获得该锁, 重新进入可执行状态.
 *
 * 缺陷: 若将一个大的方法声明为synchronized将会大大影响效率.
 * 比如: 一个对象中有 A属性 与 B属性, 此时线程一只是想修改B属性. 则对该对象上锁, 那么该对象的AB属性都被锁了. 其他线程就算只是想读取A属性, 也读取不了,
 *      因为需要等待 线程一 释放锁, 其他线程才可以操作. 那么这种情况就是 synchronized锁大了. (那么实际应该借助synchronized块 只锁 B属性, 应该是这样哈哈哈)
 *
 *
 *
 * 测试 线程安全: 即在并发时保证数据的正确性、效率尽可能高
 * synchronized
 * 1. 同步方法
 * 2. 同步块
 *
 *
 * @author PengJiaJun
 * @Date 2022/08/14 10:31
 */
public class Demo04Safe01 {
    public static void main(String[] args) {
        SafeWeb12306 safeWeb12306 = new SafeWeb12306();

        new Thread(safeWeb12306, "购票人:小明").start();
        new Thread(safeWeb12306, "购票人:小红").start();
        new Thread(safeWeb12306, "购票人:小黑").start();
    }
}

class SafeWeb12306 implements Runnable {
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
     * 加入 synchronized 后, 线程安全, 不会出现上面那种不安全的情况
     * 因为 加入 synchronized 后, 每个线程访问该方法需要获得 该对象的锁, 获取到了才可以访问, 访问结束后释放锁,
     * 这就从根本上解决了上面那种不安全的情况发生的可能, 因为不安全的原因是多个线程同时操作, 而 synchronized 保证了, 同一时刻, 只能有一个线程访问该方法
     *
     * 此处方法上的 synchronized 锁的到底是谁, 首先该方法是个成员方法, 那么就是锁 该类的对象 (也就是锁调用该方法的对象),
     * 在此处锁的应该是 this, 即this.test(); 那么从main方法来看 锁的就是 safeWeb12306对象
     * 因为 new Thread(safeWeb12306, "购票人:小明").start(); 这样new一个线程, 实际就是 safeWeb12306.run() 方法
     * 而 run()方法中又调用 test(). 那么实则调用test()的对象就是 safeWeb12306.test(); 也是 this.test();
     *
     */
    public synchronized void test() {
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