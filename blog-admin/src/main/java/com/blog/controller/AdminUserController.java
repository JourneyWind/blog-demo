package com.blog.controller;

import com.blog.domain.entity.User;
import com.blog.domain.vo.AdminUserInfoVo;
import com.blog.domain.vo.RoutersVo;
import com.blog.service.AdminLoginService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class AdminUserController {
    @Resource
    private AdminLoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        return loginService.getInfo();
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        return loginService.getRouters();
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
