package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.Menu;

import java.util.List;

/**
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
*/
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

//
//    /**
//     * 根据条件查询menu信息
//     * @return {@link ResponseResult}
//     */
//    ResponseResult selectRouterMenu();
//
//    List<Menu> selectMenuList(Menu menu);
//
//    ResponseResult getMenuList(String status, String menuName);
//
//    ResponseResult addMenu(MenuDto menuDto);
//
//    ResponseResult getMenuById(Long id);
//
//    ResponseResult updateMenu(MenuDto menuDto);
//
//    ResponseResult deleteMenu(Long menuId);
//
//    ResponseResult getMenuTree();
//
//    ResponseResult roleMenuTreeselect(Long id);


}
