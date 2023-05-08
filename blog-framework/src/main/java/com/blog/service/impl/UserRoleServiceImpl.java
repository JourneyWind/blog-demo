package com.blog.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.UserRole;
import com.blog.mapper.UserRoleMapper;
import com.blog.service.UserRoleService;
import org.springframework.stereotype.Service;


/**
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}




