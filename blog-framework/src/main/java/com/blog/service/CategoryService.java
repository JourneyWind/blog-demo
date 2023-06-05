package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.dto.CategoryDto;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.CategoryAdminVo;
import com.blog.utils.ResponseResult;

import javax.servlet.http.HttpServletResponse;

/**
* @description 针对表【sg_category(分类表)】的数据库操作Service
*/

public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    void excelExport(HttpServletResponse response);

    ResponseResult categoryList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult getCategoryOneById(Long id);

    ResponseResult updateCategory(CategoryAdminVo categoryAdminVo);

    ResponseResult deleteCategory(Long id);
}
