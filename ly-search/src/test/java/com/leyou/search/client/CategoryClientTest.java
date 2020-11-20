package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


/**
 * date:2020-10-12
 * author:zhangxiaoshuai
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Test
    public void queryBrandByCids() {
        List<Category> categories = categoryClient.queryBrandByCids(Arrays.asList(1L, 2L, 3L));
        Assert.assertEquals(3,categories.size());
        categories.forEach(category -> {
            System.out.println("category="+category);
        });

    }
}