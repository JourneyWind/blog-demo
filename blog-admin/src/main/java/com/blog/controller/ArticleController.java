package com.blog.controller;

import com.blog.domain.dto.AddArticleDto;
import com.blog.service.ArticleService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addBlog(@RequestBody AddArticleDto addArticleDto){
        return articleService.addBlog(addArticleDto);
    }

    @GetMapping("/list")
    public ResponseResult listByCondition(Integer pageNum, Integer pageSize,String title,String summary){
        return articleService.listByCondition(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult selectDetails(@PathVariable Integer id){
        return articleService.selectDetails(id);
    }
    @PutMapping
    public ResponseResult editArticle(@RequestBody AddArticleDto addArticleDto){
        return articleService.editArticle(addArticleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Integer id){
        return articleService.deleteArticle(id);
    }

}
