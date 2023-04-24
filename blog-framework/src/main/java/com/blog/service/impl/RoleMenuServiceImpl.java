//package com.blog.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.stereotype.Service;
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
//* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
//*/
//@Service
//public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
//    implements RoleMenuService{
//
//    @Resource
//    private RoleMenuMapper roleMenuMapper;
//    /**
//     * 通过id获取角色菜单id
//     *
//     * @param roleId 角色id
//     * @return {@link List}<{@link String}>
//     */
//    public List<Long> getRoleMenuIdsById(Long roleId){
//        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(RoleMenu::getRoleId,roleId);
//        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
//        List<Long> menuIds = roleMenus
//                .stream()
//                .map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
//        return menuIds;
//    }
//}
//
//
//
//
