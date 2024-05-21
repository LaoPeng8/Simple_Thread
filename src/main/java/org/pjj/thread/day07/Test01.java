package org.pjj.thread.day07;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * 线程池的五个状态
 * RUNNING - 表示线程正在运行
 * SHUTDOWN - 调用shutdown()方法后进入该状态, 该状态下会将任务执行完并且工作线程清零, 之后会进入下一状态, TIDYING
 * STOP - 调用shutdownNow()方法后进入该状态, 该状态下会直接将工作线程清零, 之后进入下一状态, TIDYING
 * TIDYING - 该状态会在阻塞队列和线程池都为空时进入
 * TERMINATED - 在terminated()方法被调用后进入该状态
 *
 * @author PengJiaJun
 * @Date 2024/05/20 16:15
 */
public class Test01 {
    public static void main(String[] args) {
        // 核心线程数: 10, 最大线程数量: 20, 线程的空闲时长: 0, 线程的空闲时长单位: 毫秒, 任务的排队队列: 基于链表实现的阻塞队列
        // 线程池的拒绝策略: 新任务就会被拒绝，并且抛出RejectedExecutionException异常(当工作队列已满(核心线程数), 且最大线程数已满)
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("业务流程...");
            }
        });

        executor.shutdown();
        executor.shutdownNow();
    }
}
