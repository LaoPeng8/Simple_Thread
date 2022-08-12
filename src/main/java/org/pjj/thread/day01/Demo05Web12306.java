package org.pjj.thread.day01;

/**
 * 共享资源, 通过实现Runnable接口, 实现多个线程的资源共享 (如果是继承Thread, 则实现不了共享资源的效果, 因为直接就是 new 本类().start()了, 自己的资源自己用了)
 *
 * 百度 与 实际测试之后得到的结论:
 *      Thread也是可以实现资源共享的, 原因: Thread类实现了Runnable接口(即 Thread也是Runnable类型的), new Thread(new Runnable(), "线程一").start();
 *      此处的 new Runnable() 同样可以传入一个 Thread对象, 因为Thread也是Runnable类型的, 从而实现资源共享
 *
 * @author PengJiaJun
 * @Date 2022/08/12 12:11
 */
public class Demo05Web12306 implements Runnable{

    private int ticketNum = 99;

    @Override
    public void run() {
        while(true) {
            if(ticketNum <= 0) {
                break;
            }

            /**
             * 当前线程 阻塞 200毫秒, 在多线程的run()方法中是不能对外throws异常的, 所以只能使用try catch
             *
             * 疑惑一个问题, 为什么没有sleep的时候不会出现并发问题, ticketNum <= 0的时候就直接退出了, 不存在ticketNum = -1的情况出现
             * 当有了sleep(200)的时候就会出现 tickNum = -1的情况出现???
             *
             * 不管有没有sleep(200), 都会出现 同一张表被两个不同的线程抢到了, 即这种情况   黄牛老王-->10, 黄牛老李-->10
             */
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName()+"-->"+ticketNum--);//打印当前线程名称同时票数-1
        }
    }

    public static void main(String[] args) {
        //一份资源
        Demo05Web12306 demo05Web12306 = new Demo05Web12306();

        //多个代理
        new Thread(demo05Web12306, "黄牛老王").start();
        new Thread(demo05Web12306, "黄牛老李").start();
        new Thread(demo05Web12306, "黄牛老张").start();

//        ====================================================================================

        //测试使用 继承 Threa的方式 实现资源共享
        Demo05Web12306WithThread demo05Web12306WithThread = new Demo05Web12306WithThread();
        new Thread(demo05Web12306WithThread, "小明").start();
        new Thread(demo05Web12306WithThread, "小红").start();
        new Thread(demo05Web12306WithThread, "小黑").start();

    }
}

//测试使用 继承 Threa的方式 实现资源共享
class Demo05Web12306WithThread extends Thread {
    private int ticketNum = 99;

    @Override
    public void run() {
        while(true) {
            if(ticketNum <= 0) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName()+"-->"+ticketNum--);//打印当前线程名称同时票数-1
        }
    }
}
