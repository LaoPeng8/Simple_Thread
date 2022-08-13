package org.pjj.thread.day02;

/**
 * 加深 join 的理解
 *
 * @author PengJiaJun
 * @Date 2022/08/13 01:10
 */
public class Demo07BlockJoin02 {
    public static void main(String[] args) {
        System.out.println("爸爸和儿子买烟的故事");
        new Thread(new Father()).start();
    }
}

class Father implements Runnable {

    @Override
    public void run() {
        System.out.println("想抽烟, 发现没了");
        System.out.println("让儿子去买华子");
        Thread son = new Thread(new Son());//开个儿子线程, 让儿子去买烟
        son.start();
        try {
            //如果此处不使用 儿子线程.join()方法, 那么会导致 儿子烟还没买回来, 老爸就已经接过了烟. (因为儿子线程 start()之后并不是马上执行, 而是等待CPU调度)
            //使用 join之后就是, 老爸让儿子去买烟, 儿子线程插队执行 且 阻塞其他线程, 等儿子线程执行完毕, 其他线程才解除阻塞 进入就绪状态, 然后CPU才会调度
            son.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("老爸接过了烟, 把零钱给了儿子");
    }
}

class Son implements Runnable {

    @Override
    public void run() {
        System.out.println("接过老爸的钱出去了...");
        System.out.println("路边有个游戏厅, 玩了10秒");
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(i + " 秒过去了...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("赶紧买烟去...");
        System.out.println("手拿一包华子回家了...");
    }
}
