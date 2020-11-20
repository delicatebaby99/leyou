package com.leyou.sms.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.leyou.sms.pojo.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * date:2020-10-18
 * author:zhangxiaoshuai
 */
@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SendUtil {

    @Autowired
    private SmsProperties prop;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String KEY_PREFIX="sms:phone:";
    private static final long SMS_MIN_INTERVAL_IN_MILLIS=60000;

    public String sendSms(String phone, String code, String signName, String template) {

//        对手机号码进行限流（同一个手机号一分钟发一次）
        String key = KEY_PREFIX + phone;
        //读取时间
        String lasTime = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(lasTime)){
            Long last = Long.valueOf(lasTime);
            if (System.currentTimeMillis()-last<SMS_MIN_INTERVAL_IN_MILLIS){
                log.info("短信服务发送短信频率过高，不能重复发送验证码，手机号码：{}",phone);
                return null;
            }
        }

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", prop.getAccessKeyId(), prop.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        request.putQueryParameter("OutId", "123");
        String data="";
        try {
            CommonResponse response = client.getCommonResponse(request);
             data = response.getData();
             log.info(data);
             //发送短信后，写入redis，指定生存时间为一分钟
            String timeValue=String.valueOf(System.currentTimeMillis());
            System.out.println(timeValue);
            redisTemplate.opsForValue().set(key,timeValue,1, TimeUnit.MINUTES);


        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return data;

    }

}
