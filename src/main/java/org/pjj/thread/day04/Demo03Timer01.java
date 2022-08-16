package org.pjj.thread.day04;

import java.util.*;

/**
 * Java自带的 定时调度
 *
 * @author PengJiaJun
 * @Date 2022/08/15 22:37
 */
public class Demo03Timer01 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        //执行安排
//        timer.schedule(new MyTask(), 1000); // 1000毫秒后执行 myTask 中的 任务(run()方法)
//        timer.schedule(new MyTask(), 1000, 200);// 1000毫秒后执行, 且每间隔200毫秒, 再次执行

        Calendar calendar = new GregorianCalendar(2099, 12, 03, 21, 53, 54);
        timer.schedule(new MyTask(),calendar.getTime());// 到达指定时间后执行
    }
}

//任务类
class MyTask extends TimerTask {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Java后端开发工程师");
        }
    }
}
