package com.leyou.order.service;

import com.leyou.common.utils.JsonUtils;
import com.leyou.order.vo.OrderStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * date:2020-10-24
 * author:zhangxiaoshuai
 */
@Service
@Slf4j
public class OrderStatusService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送延时消息到延时队列中
     * @param orderStatusMessage
     */

    public void sendMessage(OrderStatusMessage orderStatusMessage) {
        String json = JsonUtils.serialize(orderStatusMessage);
        MessageProperties properties;
        if (orderStatusMessage.getType() == 1){
            // 持久性 non-persistent (1) or persistent (2)
            properties = MessagePropertiesBuilder.newInstance().setExpiration("60000").setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        }else {
            properties = MessagePropertiesBuilder.newInstance().setExpiration("90000").setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        }

        Message message = MessageBuilder.withBody(json.getBytes()).andProperties(properties).build();
        //发送消息
        try {
            this.amqpTemplate.convertAndSend("", "leyou.order.delay.queue", message);
        }catch (Exception e){
            log.error("延时消息发送异常，订单号为：id：{}，用户id为：{}",orderStatusMessage.getOrderId(),orderStatusMessage.getUserId(),e);
        }
    }



}
