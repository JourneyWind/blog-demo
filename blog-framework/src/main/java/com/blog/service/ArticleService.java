package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.utils.ResponseResult;
import com.blog.domain.entity.Article;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

//    ResponseResult updateViewCount(Long id);
//
//    ResponseResult add(AddArticleDto article);
//
//    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);
//
//    ArticleVo getInfo(Long id);
//
//    void edit(ArticleDto article);
}
