package com.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.CommonConstants;
import com.blog.domain.entity.Menu;
import com.blog.mapper.MenuMapper;
import com.blog.service.MenuService;
import com.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Override
    public List<String> selectPermsByUserId(Long id) {
        //id为1默认管理员,返回所有权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus, CommonConstants.MENU_STATUS_NORMAL);
            wrapper.in(Menu::getMenuType, CommonConstants.MENU, CommonConstants.BUTTON);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        //idd非1用户
        //查询    userId-->roleId-->menuId-->perms
        List<String> perms = getBaseMapper().selectPermsByUserId(id);
        return perms;
    }

    @Override
    public List<Menu> selectRouterMenuTreeBuUserId(Long userId) {
        List<Menu> menus = null;
        //若是管理员，返回所有
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus, CommonConstants.MENU_STATUS_NORMAL);
            wrapper.in(Menu::getMenuType, CommonConstants.MENU, CommonConstants.DIRECTORY);
            menus = list(wrapper);
        } else {
            menus = getBaseMapper().selectRoutersByUserId(userId);
        }
        //填充tree
        List<Menu> menuTree = buildTree(menus);
        return menuTree;
    }

    private List<Menu> buildTree(List<Menu> menus) {
        List<Menu> menuList = menus.stream()
                //过滤得出顶级目录
                .filter(menu -> menu.getParentId().equals(0L))
                //设置菜单
                //menu---M    menus---M、C
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuList;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //查询子菜单的子菜单
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }


}




