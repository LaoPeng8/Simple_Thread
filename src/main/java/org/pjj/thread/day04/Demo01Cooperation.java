package org.pjj.thread.day04;

/**
 * 协作模型: 生产者消费者实现方式一: 管程法 (借助缓冲区)
 * 解耦, 提高效率
 *
 * 感觉有一点像 消息队列(RabbitMQ)
 * Controller就是生产者, 用户请求Controller, Controller不直接调用Service而是, 将消息放入 队列, Service来处理队列中的消息.
 * 流量削峰, 异步处理, 解耦
 *
 * @author PengJiaJun
 * @Date 2022/08/15 17:51
 */
public class Demo01Cooperation {
    public static void main(String[] args) {
        SynContainer synContainer = new SynContainer();//缓冲区

        new Producer(synContainer).start();//生产者
        new Consumer(synContainer).start();//消费者
    }
}

//生产者
class Producer extends Thread {
    SynContainer container;
    public Producer(SynContainer container) {
        this.container = container;
    }

    //生产
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            container.push(new SteamedBun(i));
            System.out.println("生产第 " + i + " 个馒头");
        }
    }
}

//消费者
class Consumer extends Thread {
    SynContainer container;
    public Consumer(SynContainer container) {
        this.container = container;
    }

    //消费
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            SteamedBun pop = container.pop();
            System.out.println("消费第 " + pop.getId() + " 个馒头");
        }
    }
}

//缓冲区
class SynContainer {
    SteamedBun[] steamedBuns = new SteamedBun[10];//存储容器
    int count = 0;//计数器

//    直接这样写 就是会出现 1.生产的太快了 导致消费者还没消费, 生产的就溢出数字了  2. 消费者消费的太快了, 生产者还没有生产就消费了, 数组下标出现 负数
//    //存 (生产)
//    public synchronized void push(SteamedBun steamedBun) {
//        steamedBuns[count++] = steamedBun;
//    }
//    //取 (消费)
//    public synchronized SteamedBun pop() {
//        SteamedBun steamedBun = steamedBuns[count--];
//        return steamedBun;
//    }


//    避免上面那种情况发生, 就需要控制 何时生产, 何时消费.  数组没有满就生产, 数组不为空则消费
    //存 (生产)
    public synchronized void push(SteamedBun steamedBun) {
        //何时不能生产, 容器满了
        if(count == steamedBuns.length) {
            try {
                this.wait();// 线程阻塞, 消费者通知生产时 解除阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //何时能生产, 容器中存在空间
        steamedBuns[count++] = steamedBun;
        this.notify();//存在数据了, 可以消费了 (通知消费者 进行消费)
    }
    //取 (消费)
    public synchronized SteamedBun pop() {
        // 何时消费, 容器中是否存在数据
        // 没有数据 只能等待
        if(count == 0) {
            try {
                this.wait();// 线程阻塞, 生产者通知消费时 解除阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 存在数据 可以消费
        SteamedBun steamedBun = steamedBuns[--count];
        this.notify();//存在空间了, 可以生产了 (通知生产者 进行生产)
        return steamedBun;
    }
}

//馒头(数据)
class SteamedBun {
    private int id;

    public SteamedBun(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
