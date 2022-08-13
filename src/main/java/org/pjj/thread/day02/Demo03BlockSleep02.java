package org.pjj.thread.day02;

/**
 * 利用sleep() 实现倒计时
 *
 * @author PengJiaJun
 * @Date 2022/08/12 22:58
 */
public class Demo03BlockSleep02 {
    public static void main(String[] args) throws InterruptedException {
        for(int i=10; i >= 1; i--) {
            System.out.println("倒计时: " + i);
            Thread.sleep(1000);//阻塞当前线程 1 秒, main方法也是main线程、主线程,
            //(由于没有其他线程 当前线程sleep 1秒后, 当前线程进入就绪状态等待CPU调度, CPU就只能调度该线程, 相当于该线程1秒阻塞一次, 然后指向打印, 然后阻塞)
        }
        System.out.println("恭喜你, 成为了一名合格的Java后端开发工程师");//哈哈哈哈哈哈哈哈哈哈哈哈
    }
}
