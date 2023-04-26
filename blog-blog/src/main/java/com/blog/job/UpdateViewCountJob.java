package com.blog.job;

import com.blog.domain.entity.Article;
import com.blog.service.ArticleService;
import com.blog.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blog.constants.CommonConstants.VIEW_COUNT_KEY;

@Component
public class UpdateViewCountJob {
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCountJob() {
        Map<String, Object> cacheMap = redisCache.getCacheMap(VIEW_COUNT_KEY);
        Set<Map.Entry<String, Object>> entries = cacheMap.entrySet();
        List<Article> list = entries.stream().map(
                        entry -> new Article(Long.valueOf(entry.getKey()), Long.valueOf(entry.getValue() + "")))
                .collect(Collectors.toList());

        articleService.updateBatchById(list);
    }
}
