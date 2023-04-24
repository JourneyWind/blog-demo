package com.blog.controller;

import com.blog.domain.entity.User;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.service.BlogLoginService;
import com.blog.utils.ResponseResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }


    @PostMapping("/logout")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
