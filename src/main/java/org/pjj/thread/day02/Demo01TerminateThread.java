package org.pjj.thread.day02;

/**
 * 终止线程 (让线程进入死亡状态)
 * 1. 线程正常执行完毕 -> 如for循环结束
 * 2. 外部干涉 --> 加入标志位
 *
 * 3. 不要使用 stop()、destroy()等方法, 是不安全的, 已被jdk所弃用. 建议使用 方式2. 这种方式来终止线程
 *
 * @author PengJiaJun
 * @Date 2022/08/12 18:22
 */
public class Demo01TerminateThread implements Runnable {

    //加入标志位 标记线程是否可以运行
    private boolean flag = true;

    @Override
    public void run() {
        int i = 0;
        while(flag) {
            System.out.println("i = " + i++);
        }
    }

    //对外提供方法改变标识 (以停止该线程的运行)
    public void terminate() {
        this.flag = false;
    }

    public static void main(String[] args) throws InterruptedException {
        Demo01TerminateThread demo01TerminateThread = new Demo01TerminateThread();
        new Thread(demo01TerminateThread, "线程一").start();

        //主线程阻塞 5 秒, 5秒后停止 线程一 (通过修改标志位的方式)
        Thread.sleep(5000);
        demo01TerminateThread.terminate();//线程一结束
        System.out.println("main线程结束");

    }

}
