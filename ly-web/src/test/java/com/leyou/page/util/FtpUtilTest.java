package com.leyou.page.util;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * date:2020-10-17
 * author:zhangxiaoshuai
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FtpUtilTest {

    @Test
    public void uploadFile() {
        File file = new File("F:\\leyouStudy\\upload_image\\145.html");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //参数传过来了文件和文件的输入流

            boolean success = false;
            FTPClient ftp = new FTPClient();//这是最开始引入的依赖里的方法
            ftp.setControlEncoding("utf-8");
            try {
                int reply;
                ftp.connect("192.168.43.13", 21);// 连接FTP服务器
                ftp.login("root", "123456");// 登录
                reply = ftp.getReplyCode();//连接成功会的到一个返回状态码
                System.out.println(reply);//可以输出看一下是否连接成功
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//设置文件类型
                ftp.changeWorkingDirectory("/opt/nginx/html");//修改操作空间
                //对了这里说明一下你所操作的文件夹必须要有可读权限，chomd 777 文件夹名//这里我就是用的我的home文件夹
                ftp.storeFile("145.html", inputStream);//这里开始上传文件
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    System.out.println("连接失败");

                }
                System.out.println("连接成功！");

                inputStream.close();
                ftp.logout();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {
                    }
                }
            }

        System.out.println("完成");

    }
}