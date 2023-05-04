package com.blog.service;

import com.blog.domain.entity.User;
import com.blog.utils.ResponseResult;

public interface LoginService {
    ResponseResult login(User user);
}
