package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * date:2020-10-19
 * author:zhangxiaoshuai
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {



}
