package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.Menu;
import com.blog.utils.ResponseResult;

import java.util.List;

/**
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
*/
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeBuUserId(Long userId);

    ResponseResult getMenuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    ResponseResult getMenuTree();

    ResponseResult roleMenuTreeselect(Long id);
}
