package org.pjj.thread.day02;

/**
 * yield 礼让线程, 让当前正在执行线程暂停, 不是阻塞线程, 而是将线程从 运行状态 转入 就绪状态, 让CPU调度器重新调度
 *
 * @author PengJiaJun
 * @Date 2022/08/12 23:15
 */
public class Demo04YieldDemo01 {
    public static void main(String[] args) {
        MyYield myYield = new MyYield();

        /**
         * 这里的过程是这样的
         *
         * A start() 等待调度,  B start() 等待调度.
         * 如CPU选择执行 A, 则打印 A --> start  然后 A线程 执行 Thread.yield(); 将A线程 从运行状态 转为 就绪状态
         * 此时 A 与 B 相当于又都是 处与 就绪状态, 等待CPU调度
         * CPU可以会再次执行 A, 所以就是打印 A --> end; 然后 A线程 执行完毕, 应该是进入 死亡状态
         * 然后只有线程 B 了, CPU会调度 线程B 打印 B --> start 然后执行 Thread.yield(); 将A线程 从运行状态 转为 就绪状态
         * 然后 只有线程B了, 所以CPU再次调度 线程B 打印 B --> end
         *
         */
        new Thread(myYield, "A").start();
        new Thread(myYield, "B").start();
    }
}

class MyYield implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " --> start");
        Thread.yield();//礼让线程, 将线程从 运行状态 转入 就绪状态, 让CPU调度器重新调度
        System.out.println(Thread.currentThread().getName() + " --> end");
    }
}
