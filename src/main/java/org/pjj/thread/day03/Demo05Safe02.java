package org.pjj.thread.day03;

/**
 * 同步块: synchronized(obj) { }, obj称之为同步监视器
 *      * obj 可以是任何对象, 但是推荐使用共享资源作为同步监视器
 *      * 同步方法中无需指定同步监视器, 因为同步方法的同步监视器是this即该对象本身, 或 class即类的模子
 *
 * 同步监视器的执行过程
 *      1. 第一个线程访问, 锁定同步监视器, 执行其中代码
 *      2. 第二个线程访问, 发现同步监视器被锁定, 无法访问
 *      3. 第一个线程访问完毕, 解锁同步监视器
 *      4. 第二个线程访问, 发现同步监视器未被锁定, 锁定并访问
 *
 * @author PengJiaJun
 * @Date 2022/08/14 11:52
 */
public class Demo05Safe02 {
    public static void main(String[] args) {
        Account02 account = new Account02(100, "结婚礼金");
        SafeDrawing you = new SafeDrawing(account, 80, "老公");
        SafeDrawing wife = new SafeDrawing(account, 90, "老婆");

        you.start();
        wife.start();
    }
}

//模拟取款
class SafeDrawing extends Thread {
    Account02 account;//取钱的账户
    int drawingMoney;//取的钱数
    int packetTotal;//口袋的总数

    public SafeDrawing(Account02 account, int drawingMoney, String name) {
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }

    @Override
    public void run() {
        test();
    }

//    老师说 锁资源要锁对了, 像这样锁, 锁的是 SafeDrawing 的对象, 相当于锁的提款机, 而我们操作的是 account.money
//    所以, 这样是不行的, 经过测试 确实也是没有解决并发的问题
//    public synchronized void test() {
//        if(account.money - drawingMoney < 0) {
//            return;
//        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        account.money = account.money - drawingMoney;//账户余额 = 账户余额 - 需要取的钱;
//        packetTotal = packetTotal + drawingMoney;//取的总额为 = 之前的总额 + 本次取的钱
//
//        System.out.println(this.getName()+"账户余额为:" + account.money);//getName()是父类Thread提供的
//        System.out.println(this.getName()+"口袋的钱为:" + packetTotal);
//    }

    /*
     * 老师说 锁提款机是不对的, 那么我就想到了只能锁账户, 事实证明也确实是可以锁住的.
     *
     * 想了好久, 终于明白了, 为什么此处 synchronized 写在方法上没有效果了, 是老师所谓的锁错了.
     *
     * 错误的思路: 刚开始想的是 写在方法上 锁的就是this, this就是SafeDrawing类对象, 也就是提款机, 觉得锁提款机没有什么问题
     * 锁提款机的话, 肯定是把里面的对象也锁住了, 包括账户, 这样顶多是范围大了一点,
     * 多个线程同时操作提款机 在方法上有synchronized的情况下, 同一时刻应该只有一个线程可以操作提款机, 如此来说应该是线程安全的, 那为何实际没有锁住
     *
     * 正确的思路: 首先如果是锁提款机, 得保证提款机只有一个, 但是看main方法中
     * Account02 account = new Account02(100, "结婚礼金");
     * SafeDrawing you = new SafeDrawing(account, 80, "老公"); you.start();
     * SafeDrawing wife = new SafeDrawing(account, 90, "老婆"); wife.start();
     * 一个线程一个提款机, 那么这种情况下 锁 this(提款机) 显然是没用得, 因为有两个提款机对象, 各个线程锁的是各自的this(提款机), 显然是没用的
     *
     * 正确的锁 是应该锁 账户, 因为账户只有一个, 多个线程操作同一个对象, 当老公线程操作提款机对 账户A 进行取款时, 账户A会被锁定,
     * 此时 老婆线程也操作提款机对 账户A 进行取款, 发现账户A被锁 只能阻塞, 等待解锁, 然后再操作
     *
     *
     * 用ArrayList举个例子, 如果写ArrayList的人想要ArrayList线程安全, 就可以在ArrayList的方法上加上synchronized, 使其成为同步方法, 这样可以线程安全
     * 如果 是我们使用ArrayList的人, 我们在使用ArrayList的方法上加上 synchronized, 显然是锁错了对象, 这样锁的this, 锁的是我们操作ArrayList的这个类
     * 应该要锁的是 ArrayList 对象, 这样才可以保证 ArrayList线程安全.
     *
     */
    public void test() {
        synchronized (account) {
            if(account.money - drawingMoney < 0) {
                throw new RuntimeException("需要取钱" + drawingMoney +", 实际账户余额" + account.money + ", 余额不足!!!");
//                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            account.money = account.money - drawingMoney;//账户余额 = 账户余额 - 需要取的钱;
            packetTotal = packetTotal + drawingMoney;//取的总额为 = 之前的总额 + 本次取的钱

            System.out.println(this.getName()+"账户余额为:" + account.money);//getName()是父类Thread提供的
            System.out.println(this.getName()+"口袋的钱为:" + packetTotal);
        }
    }
}

//账户
class Account02 {
    int money;//金额
    String name;//名称

    public Account02(int money, String name) {
        this.money = money;
        this.name = name;
    }
}
