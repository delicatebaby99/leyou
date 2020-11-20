package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
public interface SpecificationAPI {
    /**
     * 根据(分类，规格组，搜索条件)获取规格参数
     *
     * @param gid
     * @return
     */
    @GetMapping("spec/params")
   List<SpecParam> queryParamList(
            @RequestParam(value = "cid" ,required = false) Long cid,
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic
    );



    /**
     * 查询规格参数组，及组内参数
     * @param cid
     * @return
     */
    @GetMapping("spec/group")
    List<SpecGroup> querySpecsByCid(@RequestParam("cid") Long cid);


}
