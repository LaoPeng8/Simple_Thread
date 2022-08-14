package org.pjj.thread.day03;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 测试线程不安全的情况
 *
 * 集合线程不安全
 *
 * @author PengJiaJun
 * @Date 2022/08/14 00:07
 */
public class Demo03Unsafe03 {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                list.add(Thread.currentThread().getName());
            }).start();
        }

        /**
         * 如果不sleep, 则上面线程还在 add 的时候, 这里就打印size了, 结果是不准确的,
         * 这也是为什么使用 线程安全的 Vector, 当循环10000次时, list.size() 却只有 9997 的原因,
         *
         * 使用sleep后 是为了保证 上面的线程 add 完后, 这里再打印
         * 结果就是 ArrayList 偶尔10000, 偶尔9967 (线程不安全)
         * 使用Vector 一直是10000 (线程安全)
         *
         * 至于为什么 会是 9997 因为, arrayList线程不安全
         * 线程A list.add()后, arrayList中的下标还没移动, 线程B又 list.add()了, 就造成了覆盖, A与B的数据都存在一个下标处了.
         */
        Thread.sleep(2000);
        System.out.println(list.size());
    }
}
