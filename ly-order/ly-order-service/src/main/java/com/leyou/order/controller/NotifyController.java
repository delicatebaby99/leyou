package com.leyou.order.controller;

import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * date:2020-10-25
 * author:zhangxiaoshuai
 */
@Slf4j
@RestController
@RequestMapping("notify")
public class NotifyController {

    @Autowired
    private OrderService orderService;

    /**
     * 微信支付成功回调
     *
     * @return
     */
    @PostMapping( produces = "application/xml")
    public Map<String, String> test(@RequestBody Map<String, String> result) {
        System.out.println("微信支付成功回调");

        orderService.handleNotify(result);
        log.info("微信支付成功");
        //返回成功
        Map<String, String> msg = new HashMap<>();
        msg.put("return_code", "SUCCESS");
        msg.put("return_msg", "OK");
        return msg;
    }


}
