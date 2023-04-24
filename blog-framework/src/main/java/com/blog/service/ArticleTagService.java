package com.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.ArticleTag;

import java.util.List;

/**
* @description 针对表【sg_article_tag(文章标签关联表)】的数据库操作Service
*/
public interface ArticleTagService extends IService<ArticleTag> {
    public List<Long> getTagList(Long id);
}
