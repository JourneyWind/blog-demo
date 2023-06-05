package com.blog.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.dto.CategoryDto;
import com.blog.domain.entity.Article;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.CategoryAdminVo;
import com.blog.domain.vo.CategoryVo;
import com.blog.domain.vo.ExcelCategoryVo;
import com.blog.domain.vo.PageVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.mapper.CategoryMapper;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ExcelUtils;
import com.blog.utils.ResponseResult;
import com.blog.utils.WebUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.blog.constants.CommonConstants.ARTICLE_STATUS_PUBLISH;
import static com.blog.constants.CommonConstants.CATEGORY_STATUS_NORMAL;
import static com.blog.enums.AppHttpCodeEnum.CONTENT_IS_BLANK;

/**
 * @description 针对表【sg_category(分类表)】的数据库操作Service实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private ArticleService articleService;
    @Resource
    private CategoryService categoryService;


    /**
     * 得到类别列表
     */
    @Override
    public ResponseResult getCategoryList() {
        //1.查询文章表状态为已发布的文章列表
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
        //2.获取到文章的分类id,并且去重
        List<Article> articleList = articleService.list(queryWrapper);
        List<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .distinct()
                .collect(Collectors.toList());
        //3.根据分类id查询分类表
        List<Category> categoryList = listByIds(categoryIds);
        //4.查询状态正常的文章分类
        List<Category> categories = categoryList.stream()
                .filter(category -> CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //5.封装VO对象
        List<CategoryVo> categoryVos = BeanCopyPropertiesUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Category::getStatus, CATEGORY_STATUS_NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyPropertiesUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public void excelExport(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            ExcelUtils.setDownLoadHeader("分类", response);
            //获取需要导出的分类数据
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyPropertiesUtils.copyBeanList(list, ExcelCategoryVo.class);
            //将文件写入excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
        }
    }

    @Override
    public ResponseResult categoryList(Integer pageNum, Integer pageSize, String name, String status) {
        //1.根据文章分类名(模糊查询)和状态进行查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Category::getStatus, status);
        queryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        //2.分页查询
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        //3.将当前页中的Category对象转换为CategoryAdminVo对象
        List<Category> categories = page.getRecords();
        List<CategoryAdminVo> categoryTwoVos = BeanCopyPropertiesUtils.copyBeanList(categories, CategoryAdminVo.class);
        //4.将CategoryAdminVo对象转换为PageVo对象
        PageVo pageVo = new PageVo(categoryTwoVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        //1.首先根据分类名称查询要添加的友链是否存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Category::getName, categoryDto.getName());
        Category category = getOne(queryWrapper);
        if (!Objects.isNull(category)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CATEGORY_IS_EXIST);
        }
        //2.添加分类
        //2.1将CategoryDto对象转为Category对象
        Category addCategory = BeanCopyPropertiesUtils.copyBean(categoryDto, Category.class);
        save(addCategory);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryOneById(Long id) {
        Category category = getById(id);
        CategoryAdminVo categoryAdminVo = BeanCopyPropertiesUtils.copyBean(category, CategoryAdminVo.class);
        return ResponseResult.okResult(categoryAdminVo);
    }

    @Override
    public ResponseResult updateCategory(CategoryAdminVo categoryAdminVo) {
        //判断对象值是否为空
        if (!StringUtils.hasText(categoryAdminVo.getName()) &&
                !StringUtils.hasText(categoryAdminVo.getDescription()) &&
                !StringUtils.hasText(String.valueOf(categoryAdminVo.getStatus()))) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }
        Category category = BeanCopyPropertiesUtils.copyBean(categoryAdminVo, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_CATEGORY_FAIL);
        }
        return ResponseResult.okResult();
    }

}




