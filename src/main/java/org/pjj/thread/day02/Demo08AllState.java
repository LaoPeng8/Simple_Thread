package org.pjj.thread.day02;

/**
 * 观察线程状态
 *
 * 新生状态:  一个线程 刚 new Thread()出来就是 新生状态, 通过 .start() 进入 就绪状态
 * 就绪状态:  该状态是等待CPU调度, CPU调度到了则进入 运行状态
 * 运行状态:  运行状态被sleep(),join(),IO流.等 进入阻塞状态
 * 阻塞状态:  阻塞结束后, 进入就绪状态 (不可能再次恢复到运行状态)
 * 死亡状态: 当一个线程 run()方法结束, 或通过标志位 正常结束线程后, 该线程进入死亡状态, 进入死亡状态后不可通过start()重新进入 就绪状态
 *
 * 线程类 Thread 提供了一个 枚举 表示线程状态 Thread.State,   可以通过 线程对象.getState();获取
 * 线程状态。线程可以处于以下状态之一:
 * NEW
 *      * 尚未启动的线程状态。   (即新生状态)
 * RUNNABLE
 *      * 在Java虚拟机中执行的线程处于此状态。   (就绪状态 与 运行状态  都属于该状态)
 * BLOCKED
 *      * 被阻塞等待监视器锁定的线程处于此状态。  (即阻塞状态)
 * WAITING
 *      * 正在等待另一个线程执行特定动作的线程处于此状态。  (即阻塞状态)
 * TIMED_WAITING
 *      * 正在等待另一个线程执行动作达到等待时间的线程处于此状态。   (即阻塞状态)
 * TERMINATED
 *      * 已退出的线程处于此状态。   (即死亡状态)
 *
 *
 * @author PengJiaJun
 * @Date 2022/08/13 13:27
 */
public class Demo08AllState {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("...");
            }
        });

        Thread.State state = t.getState();
        System.out.println(state);//NEW, 此时线程刚new Thread(); 没有调用 start()方法, 所以处于 NEW 新生状态

        t.start();
        state = t.getState();
        System.out.println(state);//RUNNABLE, 此时线程调用了 start()方法, 无论CPU有没有调度 该线程, 那么 该线程都是属于 RUNNABLE状态

        while(state != Thread.State.TERMINATED) {
            Thread.sleep(1500);//只是为了打印出来的结果 方便查看
            state = t.getState();
            System.out.println(state);//TIMED_WAITING, t线程调用了 sleep(), 进入阻塞状态 且是有时间的阻塞, 所以是 TIME_WAITING
        }

        state = t.getState();
        System.out.println(state);//TERMINATED, 此时t线程执行完了, 所以是 TERMINATED 状态

    }
}
