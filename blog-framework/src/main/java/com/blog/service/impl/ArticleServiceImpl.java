package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.ArticleDetailVo;
import com.blog.domain.vo.ArticleListVo;
import com.blog.domain.vo.HotArticleVo;
import com.blog.domain.vo.PageVo;
import com.blog.mapper.CategoryMapper;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import com.blog.domain.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.blog.constants.CommonConstants.*;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult hotArticleList() {
        /*
        //1.查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //2.必须是发布的文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
        //3。按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //4.查询10条数据
        Page<Article> page = new Page<>(CURRENT_PAGE, SHOW_NUMBERS);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();
        //5.根据文章id查询redis缓存获取到当前文章的viewCount并设置
        queryViewCount(articles);
        //使用Stream流进行Bean转换操作
        List<HotArticleVo> articleVos = BeanCopyPropertiesUtils
                .copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);*/
        //1.查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //2.必须是发布的文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
        //3.按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //4. 最多显示10条数据
        queryWrapper.last("limit " + SHOW_NUMBERS);
        List<Article> list = list(queryWrapper);
        List<HotArticleVo> hotArticleVos = BeanCopyPropertiesUtils.copyBeanList(list, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);

    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //1.开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //首页 查询所有文章
        //正式发布的文章
        lambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
        //排序 置顶的文章要现实在最前面
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //如果有categoryId
        if (categoryId != null && categoryId > 0) {
            lambdaQueryWrapper.eq(Article::getCategoryId, categoryId);
        }
        List<Article> articleList = list(lambdaQueryWrapper);
        //查询获取categoryName
        articleList = articleList.stream()
                .map(article -> {
                    Category category = categoryMapper.selectById(article.getCategoryId());
                    article.setCategoryName(category.getName());
                    return article;
                }).collect(Collectors.toList());

        //封装成vo
        List<ArticleListVo> articleListVos = BeanCopyPropertiesUtils.copyBeanList(articleList, ArticleListVo.class);
        PageInfo<ArticleListVo> pageInfo = new PageInfo<>(articleListVos);
        PageVo pageVo = new PageVo(pageInfo.getList(), pageInfo.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //封装成vo
        ArticleDetailVo articleDetailVo = BeanCopyPropertiesUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Category category = categoryMapper.selectById(articleDetailVo.getCategoryId());
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

//    @Override
//    public ResponseResult updateViewCount(Long id) {
//        return null;
//    }
//
//    @Override
//    public ResponseResult add(AddArticleDto article) {
//        return null;
//    }
//
//    @Override
//    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
//        return null;
//    }
//
//    @Override
//    public ArticleVo getInfo(Long id) {
//        return null;
//    }
//
//    @Override
//    public void edit(ArticleDto article) {
//
//    }
}