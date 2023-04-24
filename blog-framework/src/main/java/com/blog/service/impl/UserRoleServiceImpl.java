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
//* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
//*/
//@Service
//public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>implements UserRoleService{
//
//    @Resource
//    private UserRoleMapper userRoleMapper;
//
//
//
//    @Override
//    public List<Long> getUserRoleById(Long id) {
//        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.select(UserRole::getRoleId).eq(UserRole::getUserId,id);
//        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
//        List<Long> roleIds = userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
//        return roleIds;
//    }
//}
//
//
//
//
