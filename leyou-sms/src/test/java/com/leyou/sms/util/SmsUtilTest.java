package com.leyou.sms.util;


import com.leyou.sms.pojo.SmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * date:2020-10-18
 * author:zhangxiaoshuai
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUtilTest {
    @Autowired
    private SendUtil sendUtil;
    @Autowired
    private SmsProperties prop;

    @Test
    public void sendSms() {
        String s = sendUtil.sendSms("18267128598", "2020", prop.getSignName(), prop.getVerifyCodeTemplate());
        System.out.println(s);

    }

    @Test
    public void test(){
        System.out.println(String.valueOf(System.currentTimeMillis()));
    }
}