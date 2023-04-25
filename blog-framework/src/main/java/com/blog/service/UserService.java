package com.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.User;
import com.blog.utils.ResponseResult;

/**
* @description 针对表【sys_user(用户表)】的数据库操作Service
*/

public interface UserService extends IService<User> {

//    ResponseResult userLogin(User user);
//
//    ResponseResult userLogout();
//
    ResponseResult userInfo();
//
//    ResponseResult updateUserInfo(User user);
//
//    ResponseResult userRegister(User user);
}
