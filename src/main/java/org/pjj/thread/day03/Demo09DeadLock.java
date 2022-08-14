package org.pjj.thread.day03;

/**
 * 死锁: 多个线程各自占有一些共享资源, 并且相互等待其他线程占有的资源释放才能进行, 而导致两个或多个线程都在等待对方释放资源, 都停止执行的情况.
 *
 * 某一个同步块同时拥有 "两个以上对象的锁时", 就有可能发生死锁的问题
 *
 * 过多的同步可能造成相互不释放资源, 从而相互等待, 一般发生于同步中持有多个对象的锁.
 *
 * 避免死锁: 不要在一个同步块中 同时持有多个对象锁 (不要锁套锁, 即 同步块 套 同步块)
 *
 * @author PengJiaJun
 * @Date 2022/08/15 01:38
 */
public class Demo09DeadLock {
    public static void main(String[] args) {
        Markup g1 = new Markup(0, "小明");
        Markup g2 = new Markup(1, "小红");
        g1.start();
        g2.start();
        /**
         * 小明涂口红
         * 小红照镜子
         *
         * 打印完这两句之后, 就一直卡着了, 既不打印也不结束, 显然是发生死锁了
         * 小明在等待 镜子锁释放
         * 小红在等待 口红锁释放
         */
    }
}

//化妆
class Markup extends Thread {
    //一个口红, 一面镜子
    static Lipstick lipstick = new Lipstick();
    static Mirror mirror = new Mirror();
    int choice;//选择
    String girl;//名字
    public Markup(int choice, String girl) {
        this.choice = choice;
        this.girl = girl;
    }

    @Override
    public void run() {
        try {
            //化妆
//            markup();//死锁
            markup222();//非死锁
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //相互持有对方的对象锁 --> 可能造成死锁
    private void markup() throws InterruptedException {
        if(choice == 0) {
            synchronized (lipstick) {//获得口红的锁
                System.out.println(this.girl + "涂口红");
                // 1 秒后想拥有镜子的锁
                Thread.sleep(1000);

                synchronized (mirror) {//获得镜子的锁
                    System.out.println(this.girl + "照镜子");
                }
            }
        } else {
            synchronized (mirror) {//获得镜子的锁
                System.out.println(this.girl + "照镜子");
                // 2 秒后想拥有口红的锁
                Thread.sleep(2000);

                synchronized (lipstick) {//获得口红的锁
                    System.out.println(this.girl + "涂口红");
                }
            }
        }
    }


    //解决死锁: 不要锁套锁
    private void markup222() throws InterruptedException {
        if(choice == 0) {
            synchronized (lipstick) {//获得口红的锁
                System.out.println(this.girl + "涂口红");
                // 1 秒后想拥有镜子的锁
                Thread.sleep(1000);

//                synchronized (mirror) {//获得镜子的锁
//                    System.out.println(this.girl + "照镜子");
//                }
            }

            //因为, 口红锁用完就释放了, 所有这里就可以获取到镜子锁了(另外一个线程就可以获取到口红锁了, 用完之后, 镜子锁就释放了)
            synchronized (mirror) {//获得镜子的锁
                System.out.println(this.girl + "照镜子");
            }
        } else {
            synchronized (mirror) {//获得镜子的锁
                System.out.println(this.girl + "照镜子");
                // 2 秒后想拥有口红的锁
                Thread.sleep(2000);

//                synchronized (lipstick) {//获得口红的锁
//                    System.out.println(this.girl + "涂口红");
//                }
            }
            synchronized (lipstick) {//获得口红的锁
                System.out.println(this.girl + "涂口红");
            }
        }

    }
}

//口红
class Lipstick {
}

//镜子
class Mirror {
}
