package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.Category;
import com.blog.utils.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @description 针对表【sg_category(分类表)】的数据库操作Service
*/

public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

}
