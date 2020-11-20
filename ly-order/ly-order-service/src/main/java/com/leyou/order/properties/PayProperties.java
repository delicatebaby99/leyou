package com.leyou.order.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 98050
 * @create: 2018-10-27 11:38
 **/
@ConfigurationProperties(prefix = "leyou.pay")
@Data
//@Configuration
//@RefreshScope
public class PayProperties {

    /**
     * 公众账号ID
     */

    private String appId;

    /**
     * 商户号
     */

    private String mchId;

    /**
     * 生成签名的密钥
     */

    private String key;

    /**
     * 连接超时时间
     */

    private int connectTimeoutMs;

    /**
     * 读取超时时间
     */

    private int readTimeoutMs;

    /**
     *
     * 回调地址
     */
    private String notifyUrl;


}
