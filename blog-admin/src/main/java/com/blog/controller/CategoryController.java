package com.blog.controller;


import com.blog.domain.dto.CategoryDto;
import com.blog.domain.vo.CategoryAdminVo;
import com.blog.service.CategoryService;
import com.blog.utils.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void excelExport(HttpServletResponse response){
        categoryService.excelExport(response);
    }

    @GetMapping("/list")
    public ResponseResult categoryList(Integer pageNum, Integer pageSize, String name, String status){
        return categoryService.categoryList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryOneById(@PathVariable Long id){
        return categoryService.getCategoryOneById(id);
    }

    @PutMapping
    public ResponseResult updateCategory(CategoryAdminVo categoryAdminVo){
        return categoryService.updateCategory(categoryAdminVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }
}
