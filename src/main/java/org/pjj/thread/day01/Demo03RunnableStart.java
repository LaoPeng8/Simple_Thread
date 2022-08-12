package org.pjj.thread.day01;

/**
 * 测试 创建线程的方式二  实现Runnable接口, 实现run()方法 (线程体),
 * 通过传入Runnable对象到Thread, 来借助Thread运行多线程
 *
 * @author PengJiaJun
 * @Date 2022/08/11 23:39
 */
public class Demo03RunnableStart implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("一边coding");
        }
    }

    public static void main(String[] args) {
        new Thread(new Demo03RunnableStart()).start();//借助Thread

        for (int i = 0; i < 20; i++) {
            System.out.println("一边music");
        }
    }
}
