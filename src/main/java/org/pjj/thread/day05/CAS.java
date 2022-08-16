package org.pjj.thread.day05;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS
 * 锁分为两类:
 *      * 悲观锁: synchronized 是独占锁即悲观锁, 会导致其他所有需要锁的线程挂起, 等待持有锁的线程释放锁.
 *      * 乐观锁: 每次不加锁 而是假设没有冲突而且完成某项操作, 如果因为冲突失败就重试, 直到成功为止
 *
 * Compare and Swap 比较并交换:  (CAS)
 *      * 乐观锁的实现
 *      * 有三个值: 一个当前内存值V、旧的预期值A、即将更新的值B。先获取到内存中的当前值V, 再将内存值V和原值A作比较,
 *          要是相等就修改为要修改的值B并返回true, 否则什么都不做, 返回false
 *      * CAS是一组原子操作, 不会被外部打断
 *      * 属于硬件级别的操作(利用CPU的CAS指令, 同时借助JNI来完成的非阻塞算法), 效率比加锁操作高
 *      * ABA问题: 如果变量V初次读取的时候是A, 并且在准备赋值的时候检查到它仍然是A, 那么能说明它的值没有被其他线程修改过吗?
 *          如果在这段期间曾经被改成B, 然后又改回A, 那CAS操作就会误认为它从来没有被修改过
 *
 * 那么我认为 CAS 应该算是一种锁, 算是乐观锁. 但是 底层是通过 C语言来实现的.
 *
 *
 * Java从JDK1.5开始提供了java.util.concurrent.atomic包，方便程序员在多线程环境下，无锁的进行原子操作。
 * 原子变量的底层使用了处理器提供的原子指令，但是不同的CPU架构可能提供的原子指令不一样，也有可能需要某种形式的内部锁,所以该方法不能绝对保证线程不被阻塞。
 *
 * 唉, 也不知道什么时候, 能够真正的看懂 使用这些 concurrent 包下的类
 * 就像以前 20年的时候的一些代码当时觉得好难, 现在22年回头再看 发现也不过如此
 *
 * @author PengJiaJun
 * @Date 2022/08/16 22:33
 */
public class CAS {
    private static AtomicInteger stock = new AtomicInteger(5);// 原子操作的 Integer
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {

                try {
                    Thread.sleep(1000);//模拟 网络延时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Integer left = stock.decrementAndGet();//相当于 --i, 但是 --i不是原子操作 不安全, 该方法是通过CAS完成的原子性的操作 --i
                if(left < 1) {
                    System.out.println("抢光了, 卖完了");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + "抢购了一件商品, 还剩" + left + "件商品");
            }).start();
        }
    }
}
