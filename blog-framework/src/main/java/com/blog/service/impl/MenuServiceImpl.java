package com.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.CommonConstants;
import com.blog.domain.entity.Menu;
import com.blog.domain.entity.RoleMenu;
import com.blog.domain.vo.MenuTreeVo;
import com.blog.domain.vo.MenuVo;
import com.blog.domain.vo.RoleMenuTreeSelectVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.mapper.MenuMapper;
import com.blog.service.MenuService;
import com.blog.service.RoleMenuService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import com.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private RoleMenuService roleMenuService;

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

    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        //1.根据menu状态和menuName查询
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status), Menu::getStatus, status)
                .like(StringUtils.hasText(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menuList = list(wrapper);
        //2.将List<Menu>对象转换为List<MenuVo>对象
        List<MenuVo> menuVos = BeanCopyPropertiesUtils.copyBeanList(menuList, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getMenuName, menu.getMenuName());
        Menu one = getOne(wrapper);
        if (!ObjectUtils.isEmpty(one)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MENU_ADD_ERROR);
        }
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getId, id);
        Menu menu = getOne(wrapper);
        MenuVo menuVo = BeanCopyPropertiesUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (!StringUtils.hasText(menu.getMenuName()) ||
                !StringUtils.hasText(menu.getMenuType()) ||
                !StringUtils.hasText(String.valueOf(menu.getStatus())) ||
                !StringUtils.hasText(menu.getPath()) ||
                !StringUtils.hasText(String.valueOf(menu.getOrderNum())) ||
                !StringUtils.hasText(menu.getIcon())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        //1.查询当前菜单是否有子菜单，如果有就不允许删除
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, id);
        List<Menu> list = list(wrapper);
        if (!ObjectUtils.isEmpty(list) && list.size() != 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_MENU_REFUSE);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuTree() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus, CommonConstants.MENU_STATUS_NORMAL);
        wrapper.in(Menu::getMenuType, CommonConstants.MENU, CommonConstants.DIRECTORY);
        List<Menu> list = list(wrapper);
        List<MenuTreeVo> menuTreeVoList = buildMenuSelectTree(list);
        return ResponseResult.okResult(menuTreeVoList);
    }

    private List<MenuTreeVo> buildMenuSelectTree(List<Menu> menuList) {
        List<MenuTreeVo> menuTreeVoList = menuList.stream()
                .map(menu -> new MenuTreeVo(menu.getId(), menu.getMenuName(), menu.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVo> list = menuTreeVoList.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(0L))
                .map(menuTreeVo -> menuTreeVo.setChildren(getMenuSelectTreeChildren(menuTreeVo, menuTreeVoList)))
                .collect(Collectors.toList());
        return list;
    }

    private List<MenuTreeVo> getMenuSelectTreeChildren(MenuTreeVo menuTreeVo, List<MenuTreeVo> menuList) {
        List<MenuTreeVo> collect = menuList.stream()
                .filter(m -> m.getParentId().equals(menuTreeVo.getId()))
                .map(m -> m.setChildren(getMenuSelectTreeChildren(m, menuList)))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        ResponseResult responseResult = getMenuTree();
        List<MenuTreeVo> list = (List<MenuTreeVo>) responseResult.getData();
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        List<RoleMenu> menuList = roleMenuService.list(wrapper);
        List<Long> collect = menuList.stream()
                .map(rm -> rm.getMenuId())
                .collect(Collectors.toList());
        RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(collect, list);
        return ResponseResult.okResult(roleMenuTreeSelectVo);
    }
}




