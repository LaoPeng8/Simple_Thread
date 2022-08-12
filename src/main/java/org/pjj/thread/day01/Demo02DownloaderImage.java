package org.pjj.thread.day01;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 加深Thread的理解
 *
 * 使用 继承Thread重写run()方法的方式 使用多线程 下载Images
 *
 * @author PengJiaJun
 * @Date 2022/08/11 22:19
 */
public class Demo02DownloaderImage extends Thread {

    private String url;
    private String fileName;

    public Demo02DownloaderImage(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            //commons-io提供的从URL下载文件到本地
            FileUtils.copyURLToFile(new URL(url), new File(System.getProperty("user.dir")+"/file/"+fileName));
            System.out.println(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Demo02DownloaderImage downloader01 = new Demo02DownloaderImage("http://upload.news.cecb2b.com/2014/0511/1399775432250.jpg", "phone.jpg");
        Demo02DownloaderImage downloader02 = new Demo02DownloaderImage("https://pics3.baidu.com/feed/f703738da97739121e605bfd253b151e377ae296.jpeg", "spl.jpeg");
        Demo02DownloaderImage downloader03 = new Demo02DownloaderImage("https://pic.gerenjianli.com/mingren_larger/1977/32092540.jpg", "lxl.jpg");

        //启动3个线程下载图片
        downloader01.start();
        downloader02.start();
        downloader03.start();


    }
}
