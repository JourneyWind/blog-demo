package com.blog.initialize;


import com.blog.constants.CommonConstants;
import com.blog.domain.entity.Article;
import com.blog.service.ArticleService;
import com.blog.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountInitialize implements CommandLineRunner {
    @Resource
    private ArticleService articleService;
    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //项目启动时将数据加载到redis
        List<Article> list = articleService.list();
        Map<String, Integer> map = list.stream().collect(Collectors.toMap(
                article -> article.getId()+"" , article -> article.getViewCount().intValue()
        ));
        redisCache.setCacheMap(CommonConstants.VIEW_COUNT_KEY,map);
    }
}
