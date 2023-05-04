package com.blog.controller;

import com.blog.domain.entity.User;
import com.blog.service.LoginService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class AdminUserController {
    @Resource
    private LoginService loginService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }
}
