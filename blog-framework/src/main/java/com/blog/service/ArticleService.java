package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.dto.AddArticleDto;
import com.blog.utils.ResponseResult;
import com.blog.domain.entity.Article;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addBlog(AddArticleDto addArticleDto);

    ResponseResult listByCondition(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult selectDetails(Integer id);

    ResponseResult editArticle(AddArticleDto addArticleDto);

    ResponseResult deleteArticle(Integer id);
}
