package com.blog.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.RoleMenu;
import com.blog.mapper.RoleMenuMapper;
import com.blog.service.RoleMenuService;
import org.springframework.stereotype.Service;


/**
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>implements RoleMenuService {

}




