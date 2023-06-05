package com.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.User;
import com.blog.utils.ResponseResult;

/**
* @description 针对表【sys_user(用户表)】的数据库操作Service
*/

public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult userRegister(User user);

    ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(User user);

    ResponseResult deleteUser(Long id);

    ResponseResult listUserInfo(Long id);

    ResponseResult updateUser(User user);
}
