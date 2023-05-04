package com.blog.service.impl;

import com.blog.domain.entity.LoginUser;
import com.blog.domain.entity.User;
import com.blog.service.LoginService;
import com.blog.utils.JwtUtils;
import com.blog.utils.RedisCache;
import com.blog.utils.ResponseResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;

import static com.blog.constants.CommonConstants.ADMIN_USER_TOKEN_KEY;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

    public ResponseResult login(User user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        //获取userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(ADMIN_USER_TOKEN_KEY + userId, loginUser);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }
}
