package com.blog.service.impl;

import com.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具有某个权限
     *
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission) {
        //如果是超级管理员,默认拥有所有权限
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
