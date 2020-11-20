package com.leyou.sms.controller;

import com.leyou.sms.pojo.SmsProperties;
import com.leyou.sms.util.SendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * date:2020-10-18
 * author:zhangxiaoshuai
 */
@RestController
public class Controller {
    @Autowired
    private SendUtil sendUtil;
    @Autowired
    private SmsProperties prop;

    @GetMapping ("/sendcode")
    public ResponseEntity<String> sendCode(){
        return ResponseEntity.ok(sendUtil.sendSms("18267128598", "123456", prop.getSignName(), prop.getVerifyCodeTemplate()));
    }
}
