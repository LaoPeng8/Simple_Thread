package org.pjj.thread.day02;

/**
 * join()合并线程, 待此线程执行完成后, 再执行其他线程, 其他线程阻塞
 *
 * @author PengJiaJun
 * @Date 2022/08/13 00:38
 */
public class Demo06BlockJoin01 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("Lambda... " + i);
            }
        });
        t.start();

        /**
         * main...19
         * Lambda... 8
         * Lambda... 9
         * Lambda... ...
         * Lambda... 99
         * main...20
         * main......
         *
         * 分析一下打印出这些结果的原因
         * 首先main线程 处于运行状态 打印19 然后 i == 20 执行了 t.join(); 此时 t线程会插队, t线程会被CPU调度, main线程会进入阻塞
         * t线程打印玩 99, 进入死亡状态, main线程阻塞完成 进入就绪状态, CPU调度main线程, 根据main线程的 程序计数器 继续执行之前未执行的部分
         * 之前 i==20时并未打印,而是让t线程执行了, 所以main线程打印 main...20, 然后main线程执行到结束
         */
        for (int i = 0; i < 100; i++) {
            //当 main 线程 执行到 i == 20 时, 会执行 t.join() 插入线程
            if(i == 20) {
                t.join();//插入线程, 待此线程执行完成后, 再执行其他线程, 其他线程在此线程执行期间 阻塞
            }
            System.out.println("main..." + i);
        }
    }
}
