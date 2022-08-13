package org.pjj.thread.day02;

/**
 * Java提供一个线程调度器来监控程序中启动后进入就绪状态的所有线程。线程调度器按照线程的优先级决定应调度哪个线程来执行。
 * 线程的优先级由数字表示, 范围从 1 到 10, (默认为 5)
 *      * Thread.MIN_PRIORITY = 1
 *      * Thread.NORM_PRIORITY = 5
 *      * Thread.MAX_PRIORITY = 10
 * int getPriority();//获取线程的优先级
 * void setPriority(int newPriority);//设置线程的优先级
 *
 * 注意: 优先级的设定建议在start()调用前
 * 注意: 优先级低只是意味着获取调度的概率低, 并不是绝对先调用优先级高的线程 再 调用优先级低的线程
 *
 * @author PengJiaJun
 * @Date 2022/08/13 15:19
 */
public class Demo09Priority {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getPriority());// 5, main线程也是由优先级的嘛, 都是默认为 5

        MyPriority myPriority = new MyPriority();
        Thread thread1 = new Thread(myPriority, "线程一");
        Thread thread2 = new Thread(myPriority, "线程二");
        Thread thread3 = new Thread(myPriority, "线程三");
        Thread thread4 = new Thread(myPriority, "线程四");
        Thread thread5 = new Thread(myPriority, "线程五");
        Thread thread6 = new Thread(myPriority, "线程六");

        //优先级的设定建议在start()调用前
        thread1.setPriority(Thread.MAX_PRIORITY);
        thread2.setPriority(Thread.MAX_PRIORITY);
        thread3.setPriority(Thread.MAX_PRIORITY);
        thread4.setPriority(Thread.MIN_PRIORITY);
        thread5.setPriority(Thread.MIN_PRIORITY);
        thread6.setPriority(Thread.MIN_PRIORITY);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

        /**
         * 优先级低只是意味着获取调度的概率低, 并不是绝对先调用优先级高的线程 再 调用优先级低的线程
         *
         * 多运行几次就可以看到 多数情况下 都是线程优先级高的先执行
         */

    }
}

class MyPriority implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " ---> " + Thread.currentThread().getPriority());
    }
}
