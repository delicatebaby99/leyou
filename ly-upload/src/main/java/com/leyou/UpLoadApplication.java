package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * date:2020-06-16
 * author:zhangxiaoshuai
 */
@EnableEurekaClient
@SpringBootApplication
public class UpLoadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpLoadApplication.class);
    }
}
