package com.blog.controller;

import com.blog.domain.dto.RoleDto;
import com.blog.domain.entity.Role;
import com.blog.service.RoleService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult roleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        return roleService.roleList(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody RoleDto roleDto){
        return roleService.changeRoleStatus(roleDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody Role role){
        return roleService.addRole(role);
    }

    /**
     * 修改角色前，角色信息回显
     */
    @GetMapping("/{id}")
    public ResponseResult getRoleInfoById(@PathVariable Long id){
        return roleService.getRoleInfoById(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }
}
