package org.pjj.thread.day04;

/**
 * 协作模型: 生产者消费者实现方式一: 信号灯法 (借助标志位)
 *
 * @author PengJiaJun
 * @Date 2022/08/15 22:11
 */
public class Demo02Cooperation {
    public static void main(String[] args) {
        Tv tv = new Tv();
        new Player(tv).start();
        new Watcher(tv).start();
    }
}

//生产者 演员
class Player extends Thread {
    Tv tv;
    public Player(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            if(i % 2 == 0) {
                this.tv.play("非诚勿扰ing...");
            }else {
                this.tv.play("女嘉宾全部熄灯...");
            }
        }
    }
}

//消费者 观众
class Watcher extends Thread {
    Tv tv;
    public Watcher(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            tv.watch();
        }
    }
}

//同一个资源 电视
class Tv{
    String voice;//电视的功能 听声音
    boolean flag = true;//信号灯; true表示 演员表演观众等待, false表示 观众观看演员等待

    //表演 (生产)
    public synchronized void play(String voice) {
        //演员等待
        if(!flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //表演
        this.voice = voice;
        System.out.println("表演了: " + voice);
        this.notify();//演员表演之后让观众继续观看 (生产者 生产之后 让消费者线程继续消费)
        flag = !flag;//切换信号灯,
    }

    //观看 (消费)
    public synchronized void watch() {
        //观众等待
        if(flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //观看
        System.out.println("听到了: " + voice);
        this.notify();//观众看完了之后让演员继续表演 (消费者 消费之后 让生产者线程继续生产)
        flag = !flag;
    }

}
