package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.dto.RoleDto;
import com.blog.domain.entity.Role;
import com.blog.utils.ResponseResult;

import java.util.List;

/**
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
*/
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeRoleStatus(RoleDto roleDto);

    ResponseResult addRole(Role role);

    ResponseResult getRoleInfoById(Long id);

    ResponseResult updateRole(Role role);

    ResponseResult deleteRole(Long id);
}
