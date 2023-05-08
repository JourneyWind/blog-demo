package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.ArticleTag;
import com.blog.mapper.ArticleTagMapper;
import com.blog.service.ArticleTagService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 针对表【sg_article_tag(文章标签关联表)】的数据库操作Service实现
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>implements ArticleTagService {
    /**
     * 得到文章标签列表
     * @param id id
     * @return {@link List}
     */
    @Test
    public List<Long> getTagList(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTagList = list(queryWrapper);

        List<Long> tags = articleTagList
                .stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        return tags;
    }
}