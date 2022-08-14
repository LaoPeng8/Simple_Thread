package org.pjj.thread.day03;

/**
 * 测试 线程不安全的情况
 *
 * 线程不安全, 余额出现了负数
 *
 * @author PengJiaJun
 * @Date 2022/08/13 19:05
 */
public class Demo02Unsafe02 {
    public static void main(String[] args) {
        Account01 account = new Account01(100, "结婚礼金");
        Drawing you = new Drawing(account, 80, "老公");
        Drawing wife = new Drawing(account, 90, "老婆");

        you.start();
        wife.start();

    }
}

//模拟取款
class Drawing extends Thread {
    Account01 account;//取钱的账户
    int drawingMoney;//取的钱数
    int packetTotal;//口袋的总数

    public Drawing(Account01 account, int drawingMoney, String name) {
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }

    /**
     * 出现了两种并发问题, 线程不安全
     * 两种情况都是因为, 老婆线程取90 但是 余额还每减的时候, 老公线程又取走80 相当于 跳过了 if(account.money - drawingMoney < 0) 判断
     * 至于余额10 是因为 老婆线程100-90=10 但是10还没从老婆的工作空间同步到主内存, 老公线程就从主内存读取到了 100 所以 100-80=20 然后又被老婆线程同步到主内存覆盖为了20
     * 余额-70 就是因为老婆线程取90 所以 100-90=10; 立马同步到了主线程, 老公线程读取到了 10-80 = -70
     *
     * 老婆账户余额为:10
     * 老公账户余额为:10
     * 老公口袋的钱为:80
     * 老婆口袋的钱为:90
     *
     * ===============
     *
     * 老婆账户余额为:-70
     * 老公账户余额为:-70
     * 老公口袋的钱为:80
     * 老婆口袋的钱为:90
     *
     */
    @Override
    public void run() {
        if(account.money - drawingMoney < 0) {
            return;
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

//账户
class Account01 {
    int money;//金额
    String name;//名称

    public Account01(int money, String name) {
        this.money = money;
        this.name = name;
    }
}
