package com.blog.service;

import com.blog.domain.entity.User;
import com.blog.domain.vo.AdminUserInfoVo;
import com.blog.domain.vo.RoutersVo;
import com.blog.utils.ResponseResult;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult<AdminUserInfoVo> getInfo();

    ResponseResult<RoutersVo> getRouters();
}
