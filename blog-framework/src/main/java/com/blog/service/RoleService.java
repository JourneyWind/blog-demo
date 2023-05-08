package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.Role;

import java.util.List;

/**
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
*/
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

}
