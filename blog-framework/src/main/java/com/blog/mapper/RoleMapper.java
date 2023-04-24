package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}

