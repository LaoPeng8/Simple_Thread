package org.pjj.thread.day02;

/**
 * 守护线程
 *
 * 线程分为用户线程 和 守护线程
 * 虚拟机必须确保用户线程执行完毕
 * 虚拟机不用等待守护线程执行完毕
 * 如后台记录操作日志、监控内存使用等
 *
 * 默认都是用户线程, JVM需要等待所有用户线程执行完毕才会停止 (不会等待守护线程, 只要用户线程全部执行完毕, JVM即停止)
 *
 *
 * @author PengJiaJun
 * @Date 2022/08/13 15:51
 */
public class Demo10Daemon {
    public static void main(String[] args) {
        Thread god = new Thread(()->{
            while(true) {
                System.out.println("bless you");
            }
        });//上帝线程, 上帝永生 死循环

        Thread you = new Thread(() -> {
            for(int i=0; i <= 365 * 80; i++) {
                System.out.println("happy life");//开开心心每一天
            }
            System.out.println("ooooo...");
        });//普通人线程, 普通人活80岁, for循环 365 * 80

        god.setDaemon(true);//设置线程为守护线程
        god.start();//上帝先运行
        you.start();

        /**
         * JVM确保用户线程全部执行完毕后会停止运行, 不用管守护线程
         * 所有 you 线程执行完毕后, god 线程 也会随之结束 (尽管是死循环, JVM都关闭了, god线程肯定也挂了)
         */
    }
}
