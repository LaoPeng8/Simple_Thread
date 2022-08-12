package org.pjj.thread.day01;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.*;

/**
 * 测试 创建线程的方式三  实现Callable接口, 实现call()方法 (线程体)
 *
 * Callable接口 与 Runnable接口的区别
 * call方法可以抛出异常, 可以有返回值
 * run方法不能抛出异常, 空返回值
 *
 * 使用 实现Callable接口, 实现call()方法 (线程体) 的方式 使用多线程 下载Images
 *
 *
 *
 * @author PengJiaJun
 * @Date 2022/08/12 13:16
 */
public class Demo07DownloaderImage implements Callable<Boolean> {

    private String url;
    private String fileName;

    public Demo07DownloaderImage(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    @Override
    public Boolean call() throws Exception {
        //commons-io提供的从URL下载文件到本地
        FileUtils.copyURLToFile(new URL(url), new File(System.getProperty("user.dir")+"/file/"+fileName));
        System.out.println(fileName);
        return true;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1. 创建目标对象
        Demo07DownloaderImage demo07DownloaderImage1 = new Demo07DownloaderImage("http://upload.news.cecb2b.com/2014/0511/1399775432250.jpg", "phone.jpg");
        Demo07DownloaderImage demo07DownloaderImage2 = new Demo07DownloaderImage("https://pics3.baidu.com/feed/f703738da97739121e605bfd253b151e377ae296.jpeg", "spl.jpeg");
        Demo07DownloaderImage demo07DownloaderImage3 = new Demo07DownloaderImage("https://pic.gerenjianli.com/mingren_larger/1977/32092540.jpg", "lxl.jpg");

        //2. 创建执行服务
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //3. 提交执行
        Future<Boolean> result1 = executorService.submit(demo07DownloaderImage1);
        Future<Boolean> result2 = executorService.submit(demo07DownloaderImage2);
        Future<Boolean> result3 = executorService.submit(demo07DownloaderImage3);

        //4. 获取结果
        Boolean r1 = result1.get();
        Boolean r2 = result2.get();
        Boolean r3 = result3.get();

        //5. 关闭服务
        executorService.shutdown();
    }
}
