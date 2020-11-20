package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CodeUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * date:2020-10-18
 * author:zhangxiaoshuai
 */
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String PREFIX = "user:phone:";

    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnums.INVALID_USER_TYPE);
        }

        return userMapper.selectCount(user) == 0;
    }

    public void sendCode(String phone) {
        //生存key
        String key = PREFIX + phone;

        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);

        amqpTemplate.convertAndSend("leyou.sms.exchange", "sms.verify.code", msg);
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    public Boolean register(User user, String code) {
//        从redis中获取验证码
        String key = PREFIX + user.getPhone();
        String chackCode = this.redisTemplate.opsForValue().get(key);
        if (!code.equals(chackCode)) {
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
//      生成盐
        String salt = CodeUtils.generateSalt();
        user.setSalt(salt);
        //对密码进行加密
        user.setPassword(CodeUtils.md5Hex(user.getPassword(), salt));
        //写入数据库

        int flag = this.userMapper.insertSelective(user);
        //注册成功后删除redis中code
        if (flag == 1) {
            try {
                this.redisTemplate.delete(key);
                return true;
            } catch (Exception e) {
                log.error("删除缓存验证码失败，code：{}", chackCode, e);
            }
        }
        return false;
    }

    public User queryUser(String username, String password) {
        log.info("正在查询用户..."+username);
        User record = new User();
        record.setUsername(username);
        //根据用户名进行查询
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        //校验密码(判断加密后的密码是否一致)
        //密码不正确
        if (!user.getPassword().equals(CodeUtils.md5Hex(password,user.getSalt()))) {
            return null;
        }
        //用户名密码正确
        return user;
    }
}
