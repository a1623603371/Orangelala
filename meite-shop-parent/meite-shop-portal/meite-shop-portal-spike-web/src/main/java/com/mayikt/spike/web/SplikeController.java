package com.mayikt.spike.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: 秒杀相关页面
 * @Package: com.mayikt.spike.web
 * @ClassName: SplikeController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/3 17:24
 * @Version: 1.0
 */
@RestController
public class SplikeController {
    /**
     * 秒杀商品详情页
     * @param id
     * @return
     */
    @RequestMapping("details/{id}")
    public String details(@PathVariable("id") Long id){
        return "details";
    }
}
