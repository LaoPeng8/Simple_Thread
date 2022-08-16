package org.pjj.thread.day04;

/**
 * ThreadLocal: 每个线程自身的存储区域 (本地区域 | 局部区域), 每个线程存储自身的数据, 更改不会影响其他线程
 * get/set/initialValue
 *
 * @author PengJiaJun
 * @Date 2022/08/16 01:06
 */
public class Demo04ThreadLocal01 {
//    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
/*    private static ThreadLocal<Integer> threadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return 200;
        }
    };*/
    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 200);

    public static void main(String[] args) {
        // threadLocal.get() 获取值
        System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());//200

        // threadLocal.set(99); 设置值
        threadLocal.set(99);
        System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());//99

        new Thread(()->{
            threadLocal.set(888);
            System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());//888
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //结果还是 99, 这说明 各个线程的 threadLocal设置的值是独立的, 上面其他线程设置的值888, 并不影响 main线程的 99
        System.out.println(Thread.currentThread().getName() + " --> " + threadLocal.get());//99

    }
}
