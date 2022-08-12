package org.pjj.thread.day01;

/**
 * 龟兔赛跑  --- 加深印象 -- 使用Runnable接口实现资源共享
 *
 * @author PengJiaJun
 * @Date 2022/08/12 12:52
 */
public class Demo06Racer implements Runnable {

    private String winner;

    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {

            /**
             * 让兔子跑10步睡10毫秒
             * 可能是现在的处理器太快了, 让谁休息, 那么另一方就直接跑满100步
             */
            if(Thread.currentThread().getName().equals("乌龟") && i % 10 == 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(Thread.currentThread().getName()+"-->"+i);
            if(isWinner(i)) {
                break;
            }
        }
    }

    public boolean isWinner(int step) {
        if(winner != null) {
            return true;//存在胜利者
        }else {
            if(step == 100) {
                winner = Thread.currentThread().getName();
                System.out.println("winner is " + winner);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Demo06Racer demo06Racer = new Demo06Racer();

        new Thread(demo06Racer, "兔子").start();
        new Thread(demo06Racer, "乌龟").start();

        /**
         * 这游戏有点东西, 前99步是谁跑的不重要, 只要第100步是谁跑的, 谁就是胜利者, 哈哈哈哈哈哈哈哈哈
         */
    }
}
