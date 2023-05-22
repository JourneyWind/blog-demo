package com.blog.controller;

import com.blog.domain.dto.AddArticleDto;
import com.blog.service.ArticleService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("//content/Article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addBlog(@RequestBody AddArticleDto addArticleDto){
        return articleService.addBlog(addArticleDto);
    }
}
