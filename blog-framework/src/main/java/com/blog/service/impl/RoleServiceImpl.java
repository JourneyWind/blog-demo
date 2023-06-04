package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.dto.RoleDto;
import com.blog.domain.entity.Role;
import com.blog.domain.entity.RoleMenu;
import com.blog.domain.vo.PageVo;
import com.blog.domain.vo.RoleVo;
import com.blog.mapper.RoleMapper;
import com.blog.service.RoleMenuService;
import com.blog.service.RoleService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private RoleService roleService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 是则返回admin
        if (id != null && id == 1L) {
            return Arrays.asList("admin");
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName)
                .eq(StringUtils.hasText(status), Role::getStatus, status);
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        Page<Role> page = getBaseMapper().selectPage(rolePage, wrapper);
        List<RoleVo> roleVos = BeanCopyPropertiesUtils.copyBeanList(page.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos, page.getTotal()));
    }

    @Override
    public ResponseResult changeRoleStatus(RoleDto roleDto) {
        LambdaUpdateWrapper<Role> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Role::getId, roleDto.getRoleId())
                .set(Role::getStatus, roleDto.getStatus());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(Role role) {
        //TODO 判断角色是否存在
        save(role);
        Long[] menuIds = role.getMenuIds();
        Long id = role.getId();
        for (Long menuId : menuIds) {
            roleMenuService.save(new RoleMenu(id, menuId));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleInfoById(Long id) {
        //1.根据用户id获取到当前用户信息
        Role role = roleService.getById(id);
        //2.将Role对象转换为RoleVo对象
        RoleVo roleVo = BeanCopyPropertiesUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(Role role) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getId,role.getId());
        update(role, lambdaQueryWrapper);
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        Long roleId = role.getId();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(wrapper);
        for (Long id : role.getMenuIds()) {
            roleMenuService.save(new RoleMenu(roleId, id));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        //1.首先移除当前角色所关联的sys_role_menu表数据
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(wrapper);
        //2.根据id移除角色
        removeById(id);
        return ResponseResult.okResult();
    }


}

