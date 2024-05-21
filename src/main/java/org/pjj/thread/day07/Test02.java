package org.pjj.thread.day07;

/**
 * 中断并不是说可以直接停止线程的运行, 实际上还是靠"标志位"来控制线程的停止 (假如线程体中根本不关心不处理中断信号, 那么在外部通过线程对线再怎么调中断方法也没用)
 * 也就是说, 假如我线程中正在执行某一耗时操作2分钟, 此时中断该线程, 也得需要该耗时操作执行完后, 才能修改标志位让下一次循环直接结束
 *
 * 线程在阻塞状态时收到了中断信号, 例如在sleep时, 是会抛出 java.lang.InterruptedException: sleep interrupted 异常的
 * @author PengJiaJun
 * @Date 2024/05/20 16:30
 */
public class Test02 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            boolean flag = true;
            @Override
            public void run() {
                int i = 1;
                while(flag) {
                    try {
                        Thread.sleep(1000);
                        System.out.printf("业务...%d\n", i++);
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
//                        flag = false;
                        return;
                    }

                    if(Thread.currentThread().isInterrupted()) {
                        flag = false;
                    }
                }
            }
        });

        t1.start();

        try {
            Thread.sleep(3000);
            t1.interrupt();
            System.out.println("中断t1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
