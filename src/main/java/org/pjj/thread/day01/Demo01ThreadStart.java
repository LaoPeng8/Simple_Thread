package org.pjj.thread.day01;

/**
 * 测试 创建线程的方式一  继承Thread类, 重写run()方法 (线程体)
 *
 *
 * @author PengJiaJun
 * @Date 2022/08/11 22:07
 */
public class Demo01ThreadStart extends Thread {

    @Override
    public void run() {
        for(int i=0; i < 20; i++) {
            System.out.println("一边coding");
        }
    }

    public static void main(String[] args) {

        /**
         * 执行线程必须调用start(),加入到调度器
         * 不一定马上执行, 系统安排调度分配执行
         * 直接调用run()不是开启多线程, 是普通方法的调用
         */
        Demo01ThreadStart demo01ThreadStart = new Demo01ThreadStart();
        demo01ThreadStart.start();//启动该线程, 即线程从 New 状态切换到 就绪状态 (就绪状态意味等待CPU进行调度的状态)
//        demo01ThreadStart.run();

        for (int i = 0; i < 20; i++) {
            System.out.println("一边music");
        }
        //结果为:
        //一边music...
        //一边coding...
        //一边music...
        //因为多线程进行, 是否调用该线程是由CPU调度器绝对的, 当执行线程music时, cpu调度到执行coding, 后又继续执行music


    }
}
