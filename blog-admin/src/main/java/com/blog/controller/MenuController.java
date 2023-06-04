package com.blog.controller;

import com.blog.domain.entity.Menu;
import com.blog.service.MenuService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult getMenuList(String status,String menuName){
        return menuService.getMenuList(status,menuName);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    /**
     * 更新menu前，通过id查询menu信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable Long id){
        return menuService.deleteMenu(id);
    }
}
