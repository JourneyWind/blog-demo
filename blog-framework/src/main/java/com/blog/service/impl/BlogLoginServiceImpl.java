package com.blog.service.impl;

import com.blog.domain.entity.LoginUser;
import com.blog.domain.entity.User;
import com.blog.domain.vo.BlogUserLoginVo;
import com.blog.domain.vo.UserInfoVo;
import com.blog.service.BlogLoginService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.JwtUtils;
import com.blog.utils.RedisCache;
import com.blog.utils.ResponseResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.blog.constants.CommonConstants.BLOG_USER_TOKEN_KEY;


@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        //获取userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(BLOG_USER_TOKEN_KEY + userId, loginUser);
        //把token和userInfo封装 返回
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,
                BeanCopyPropertiesUtils.copyBean(loginUser.getUser(), UserInfoVo.class));
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取userId
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        redisCache.deleteObject(BLOG_USER_TOKEN_KEY + userId);
        return ResponseResult.okResult();
    }
}
