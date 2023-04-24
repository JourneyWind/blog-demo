package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.Article;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.CategoryVo;
import com.blog.mapper.CategoryMapper;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static com.blog.constants.CommonConstants.ARTICLE_STATUS_PUBLISH;
import static com.blog.constants.CommonConstants.CATEGORY_STATUS_NORMAL;

/**
 * @description 针对表【sg_category(分类表)】的数据库操作Service实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private ArticleService articleService;
//
//    @Resource
//    private CategoryMapper categoryMapper;


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

//
//    /**
//     * 查询文章类别列表
//     *
//     * @param pageNum     页面num
//     * @param pageSize    页面大小
//     * @param categoryDto 类别dto
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
//        //        1.根据文章分类名(模糊查询)和状态进行查询
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(StringUtils.hasText(categoryDto.getStatus()), Category::getStatus, categoryDto.getStatus());
//        queryWrapper.like(StringUtils.hasText(categoryDto.getName()), Category::getName, categoryDto.getName());
//
////        2.分页查询
//        Page<Category> page = new Page<>(pageNum, pageSize);
//        page(page, queryWrapper);
//
////        3.将当前页中的Category对象转换为CategoryTwoVo对象
//        List<Category> categories = page.getRecords();
//        List<CategoryTwoVo> categoryTwoVos = BeanCopyPropertiesUtils.copyBeanList(categories, CategoryTwoVo.class);
////        4.将CategoryTwoVo对象转换为AdminCategoryVo对象
//        AdminCategoryVo adminCategoryVo = new AdminCategoryVo(categoryTwoVos, page.getTotal());
//        return ResponseResult.okResult(adminCategoryVo);
//    }
//
//
//    /**
//     * 添加类别
//     *
//     * @param categoryDto 类别dto
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult addCategory(CategoryDto categoryDto) {
//        //        1.首先根据分类名称查询要添加的友链是否存在
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(Category::getName, categoryDto.getName());
//        Category category = getOne(queryWrapper);
//        if (!Objects.isNull(category)) {
//            return ResponseResult.errorResult(CATEGORY_IS_EXIST);
//        }
////        2.添加分类
////          2.1将CategoryDto对象转为Category对象
//        Category addCategory = BeanCopyPropertiesUtils.copyBean(categoryDto, Category.class);
//        save(addCategory);
//        return ResponseResult.okResult();
//    }
//
//
//    /**
//     * 通过id查询分类
//     *
//     * @param id id
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult getCategoryOneById(Long id) {
//        //        1.根据id查询友链
//        Category category = getById(id);
////        2.将Link对象封装为LinkVo对象
//        CategoryTwoVo categoryVo = BeanCopyPropertiesUtils.copyBean(category, CategoryTwoVo.class);
//        return ResponseResult.okResult(categoryVo);
//    }
//
//    /**
//     * 修改文章分类信息
//     *
//     * @param categoryDto 类别dto
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult updateCategory(CategoryDto categoryDto) {
//        //        1.判断LinkVo对象值是否为空
//        if (!StringUtils.hasText(categoryDto.getName()) &&
//                !StringUtils.hasText(categoryDto.getDescription()) &&
//                !StringUtils.hasText(String.valueOf(categoryDto.getStatus()))) {
//            return ResponseResult.errorResult(CONTENT_IS_BLANK);
//        }
////        2.将LinkVo对象转换为Link对象
//        Category category = BeanCopyPropertiesUtils.copyBean(categoryDto, Category.class);
//        updateById(category);
//        return ResponseResult.okResult();
//    }
//
//    /**
//     * 删除文章分类
//     *
//     * @param id id
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult deleteCategory(Long id) {
//        boolean result = removeById(id);
//        if (result == false) {
//            return ResponseResult.errorResult(DELETE_CATEGORY_FAIL);
//        }
//        return ResponseResult.okResult();
//    }
//
//    /**
//     * 查询文章类别列表
//     *
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult getNameArticleCategoryList() {
////        1.查询所有文章类别列表
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Category::getStatus, CATEGORY_STATUS_NORMAL);
//        List<Category> categories = categoryMapper.selectList(queryWrapper);
////        2.将Category对象转换为CategoryVo对象
//        List<CategoryVo> categoryVos = BeanCopyPropertiesUtils.copyBeanList(categories, CategoryVo.class);
//        return ResponseResult.okResult(categoryVos);
//    }
//
//    @Override
//    public void getExcelData(String filename, HttpServletResponse response){
////        使用工具类操作Excel
////        1.设置下载文件的请求头
//        try {
//            DownLoadExcelUtils.setDownLoadHeader(filename, response);
////        2.获取需要导出的数据
//            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Category::getStatus, CATEGORY_STATUS_NORMAL);
//            List<Category> categories = categoryMapper.selectList(queryWrapper);
////        3.将Category对象转化为DownloadDataVo对象
//            List<DownloadDataVo> downloadDataVos = BeanCopyPropertiesUtils.copyBeanList(categories, DownloadDataVo.class);
////        3.将数据写入到Excel中
//            EasyExcel.write(response.getOutputStream(), DownloadDataVo.class).autoCloseStream(Boolean.FALSE).sheet("文章分类")
//                    .doWrite(downloadDataVos);
//
//        } catch (Exception e) {
//            ResponseResult result = ResponseResult.errorResult(SYSTEM_ERROR);
//            WebUtils.renderString(response,JSON.toJSONString(result));
//        }
//    }
//
//
//    /**
//     * 得到类别名称
//     *
//     * @param id id
//     * @return {@link String}
//     */
//    @Override
//    public String getCategoryName(Long id) {
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Category::getId,id);
//        String categoryName = getOne(queryWrapper).getName();
//        return categoryName;
//    }


}




