package com.leyou.page.controller;

import com.leyou.page.service.PageHtmlService;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * date:2020-10-16
 * author:zhangxiaoshuai
 */
@Controller
@RequestMapping("item")
public class PageController {
    @Autowired
    private PageService pageService;
@Autowired
private PageHtmlService pageHtmlService;
    /**
     * 跳转到商品详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
//@ResponseBody
    public String toItemPage(Model model, @PathVariable("id")Long id){
        Map<String, Object> attributes = pageService.loadData(id);
        model.addAllAttributes(attributes);
        //页面静态化
        this.pageHtmlService.asyncExcute(id);
        return "item";
    }
}
