package com.blog.controller;

import com.blog.domain.entity.User;
import com.blog.service.UserService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 查询用户信息
     */
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult userRegister(@RequestBody User user) {
        return userService.userRegister(user);
    }


}
